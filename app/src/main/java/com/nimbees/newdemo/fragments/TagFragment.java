package com.nimbees.newdemo.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonRectangle;
import com.nimbees.newdemo.R;
import com.nimbees.newdemo.activity.NavigationActivity;
import com.nimbees.newdemo.adapters.SwipeableRecyclerViewTouchListener;
import com.nimbees.newdemo.adapters.ValuesAdapter;
import com.nimbees.platform.NimbeesClient;
import com.nimbees.platform.NimbeesException;
import com.nimbees.platform.beans.NimbeesTag;
import com.nimbees.platform.callbacks.NimbeesCallback;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.ArrayList;
import java.util.List;

import es.gloin.nimbees.common.beans.tag.TagBean;


/**
 * This TagFragment lets see all the tags that belongs to your app
 * and the values that you have defined.
 * <p/>
 * Say your preferences to the bees!
 */

public class TagFragment extends Fragment {

    /** Index to remember the position of the selected item in the list. */
    private int mSelectedTagIndex = 0;

    /** Spinner element to show all Tags. */
    private Spinner mSpinner;

    /** Wheel to show the loading process. */
    private ProgressWheel mWheel;

    /** Button used to load the Tags from the server. */
    private ButtonRectangle mButtonGetTags;

    /** Button used to add a Value to a Tag. */
    private ButtonRectangle mButtonAddValue;

    /** RecyclerView to be used with the values of Tags. */
    private RecyclerView mRecyclerView;

    /** View to show when no values are defined for the selected tag. */
    private TextView mEmptyView;

    /** LinearLayout to be used with the RecyclerView. */
    private LinearLayoutManager mLayoutManager;

    /** List of Tag names. */
    private ArrayList<String> mItemNames;

    /** List of Tags. */
    private List<NimbeesTag> mTagList;

    /** List of values of each Tag. */
    private List<String> mTagValues;

    /** ArrayAdapter to show the Tag names in the spinner. */
    private ArrayAdapter<String> mAdapter;

    /** Adapter to show values of tags in UI. */
    private ValuesAdapter mItemsAdapter;

    /** LinearLayout with the list of tags and their values. */
    private View mTagContentLayout;

    /** LinearLayout shown when there are no tags. */
    private View mNoTagsLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTagValues = new ArrayList<String>();
        mItemNames = new ArrayList<String>();

        // Call getTags() to populate the items list using the adapter
        mTagList = NimbeesClient.getTagManager().getTags();
        ((NavigationActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.title_section3));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tag, container, false);

        // start UI elements
        mSpinner = (Spinner) view.findViewById(R.id.spinner);
        mButtonGetTags = (ButtonRectangle) view.findViewById(R.id.button_get_tags);
        mButtonAddValue = (ButtonRectangle) view.findViewById(R.id.add_value_button);
        mWheel = (ProgressWheel) view.findViewById(R.id.progress_wheel_tags);

        mTagContentLayout = view.findViewById(R.id.tags_content);
        mNoTagsLayout = view.findViewById(R.id.tags_empty);

        if (mTagList.isEmpty()) {
            mTagContentLayout.setVisibility(View.GONE);
            mNoTagsLayout.setVisibility(View.VISIBLE);
        } else {
            mTagContentLayout.setVisibility(View.VISIBLE);
            mNoTagsLayout.setVisibility(View.GONE);
        }

        // Adapter and list of Tags
        mRecyclerView = (RecyclerView) view.findViewById(R.id.tag_recycler_view);
        mEmptyView = (TextView) view.findViewById(android.R.id.empty);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mItemsAdapter = new ValuesAdapter(mItemNames);
        mRecyclerView.setAdapter(mItemsAdapter);

        // Adapter and list for the Spinner element
        mAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.spinner_item, mTagValues);
        mAdapter.setDropDownViewResource(R.layout.spinner_list);
        mSpinner.setAdapter(mAdapter);
        setNimbeesTagOnSpinner(mTagList);

        // We do invisible the wheel
        mWheel.setVisibility(View.INVISIBLE);

        // Listener to get Cards Swipe and delete values of each TAG
        SwipeableRecyclerViewTouchListener swipeTouchListener =
                new SwipeableRecyclerViewTouchListener(mRecyclerView,
                        new SwipeableRecyclerViewTouchListener.SwipeListener() {
                            @Override
                            public boolean canSwipe(int position) {
                                return true;
                            }

                            @Override
                            public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    removeValueFromTag(mTagValues.get(mSelectedTagIndex).toString(), mItemNames.get(position));
                                    mItemNames.remove(position);
                                    if (mItemNames.isEmpty()) {
                                        mRecyclerView.setVisibility(View.GONE);
                                        mEmptyView.setVisibility(View.VISIBLE);
                                    } else {
                                        mRecyclerView.setVisibility(View.VISIBLE);
                                        mEmptyView.setVisibility(View.GONE);
                                    }
                                    mItemsAdapter.notifyDataSetChanged();
                                }

                            }

                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    removeValueFromTag(mTagValues.get(mSelectedTagIndex).toString(), mItemNames.get(position));
                                    mItemNames.remove(position);
                                    if (mItemNames.isEmpty()) {
                                        mRecyclerView.setVisibility(View.GONE);
                                        mEmptyView.setVisibility(View.VISIBLE);
                                    } else {
                                        mRecyclerView.setVisibility(View.VISIBLE);
                                        mEmptyView.setVisibility(View.GONE);
                                    }
                                }
                                mItemsAdapter.notifyDataSetChanged();
                            }

                        });

        mRecyclerView.addOnItemTouchListener(swipeTouchListener);

        // Listener of the spinner to do actions on a item selected
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                mWheel.setVisibility(View.VISIBLE);
                loadDatafromTag(mTagValues.get(i).toString());
                mSelectedTagIndex = i;

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // Do nothing
            }

        });

        // Button to add a value to a TAG
        mButtonAddValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(mTagValues.get(mSelectedTagIndex).toString());
            }
        });

        // Button to get Tags from Server
        mButtonGetTags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWheel.setVisibility(View.VISIBLE);
                loadTagsFromServer();
            }
        });
        return view;
    }


    /**
     * Set the list of Tags in the Spinner
     *
     * @param mTagList the list of tags
     */
    private void setElementsOnSpinner(List<TagBean> mTagList) {

        mTagValues.clear();
        for (TagBean ctb : mTagList) {
            mTagValues.add(ctb.getName());
        }
        mAdapter.notifyDataSetChanged();
    }

    /**
     * Set the list of Tags in the Spinner
     *
     * @param mTagList the list of NimbeesTag
     */
    private void setNimbeesTagOnSpinner(List<NimbeesTag> mTagList) {

        mTagValues.clear();
        for (NimbeesTag ctb : mTagList) {
            mTagValues.add(ctb.getName());
        }
        mAdapter.notifyDataSetChanged();
    }

    /**
     * Set the list of values in the list of a Tag
     *
     * @param stringList the list of the values
     */
    private void setValuesOnList(List<String> stringList) {

        mItemNames.clear();
        for (String s : stringList) {
            mItemNames.add(s);
        }
        mItemsAdapter.notifyDataSetChanged();
    }

    /**
     * Load in the device all the Tags from the Server
     */
    private void loadTagsFromServer() {

        NimbeesClient.getTagManager().loadTagsFromServer(new NimbeesCallback<List<TagBean>>() {
            @Override
            public void onSuccess(List<TagBean> tagBeans) {

                if (tagBeans.isEmpty()) {
                    mTagContentLayout.setVisibility(View.GONE);
                    mNoTagsLayout.setVisibility(View.VISIBLE);
                } else {
                    mTagContentLayout.setVisibility(View.VISIBLE);
                    mNoTagsLayout.setVisibility(View.GONE);
                }

                setElementsOnSpinner(tagBeans);
                mWheel.setVisibility(View.INVISIBLE);
                Toast.makeText(getActivity().getApplicationContext(), "Tags recovered : " + String.valueOf(tagBeans.size()), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(NimbeesException e) {
                Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.error_loading_tags), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Load in the device all the values of given tag
     *
     * @param tagname the name of the tag
     */
    private void loadDatafromTag(String tagname) {

        NimbeesClient.getTagManager().getServerTagValues(tagname, new NimbeesCallback<List<String>>() {
            @Override
            public void onSuccess(List<String> strings) {
                if (strings.isEmpty()) {
                    mRecyclerView.setVisibility(View.GONE);
                    mEmptyView.setVisibility(View.VISIBLE);
                } else {
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mEmptyView.setVisibility(View.GONE);
                }
                setValuesOnList(strings);
                mWheel.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(NimbeesException e) {
                Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.error_loading_tag_values), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Assign a value to a Tag
     *
     * @param tagname  the name of the tag
     * @param tagvalue the value to add to the tag
     */
    private void assignValueToTag(final String tagname, String tagvalue) {

        NimbeesClient.getTagManager().addServerTagValue(tagname, tagvalue, new NimbeesCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                loadDatafromTag(tagname);
            }

            @Override
            public void onFailure(NimbeesException e) {
                Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.error_adding_tag_value), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Remove a value from a Tag
     *
     * @param tagName  the name of the tag
     * @param tagValue the value to remove from the tag
     */
    private void removeValueFromTag(final String tagName, final String tagValue) {

        NimbeesClient.getTagManager().deleteServerTagValue(tagName, tagValue, new NimbeesCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                // Do nothing
            }

            @Override
            public void onFailure(NimbeesException e) {
                // Re-add the value that was deleted, because deletion failed
                mItemNames.add(tagValue);
                mRecyclerView.setVisibility(View.VISIBLE);
                mEmptyView.setVisibility(View.GONE);
                Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.error_removing_tag_value), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Select a proper dialog to show it based on tag type
     *
     * @param tagName name of the tag
     */
    private void showDialog(final String tagName) {

        String valuetype = "";

        for (NimbeesTag nt : mTagList) {

            if (nt.getName().equals(tagName)) {
                valuetype = nt.getValueType();
                continue;
            }
        }
        switch (valuetype) {
            case "TEXT":
                showTextDialog(tagName);
                break;
            case "FLOAT":
                showFloatDialog(tagName);
                break;
            case "INTEGER":
                showIntegerDialog(tagName);
                break;
            case "BOOLEAN":
                showBooleanDialog(tagName);
                break;
            default:
                showUnrecognisedTagTypeDialog();
                break;
        }
    }

    /**
     * Shows a dialog informing the user that the selected tag has an invalid type
     */
    void showUnrecognisedTagTypeDialog() {
        AlertDialog ad = new AlertDialog.Builder(getActivity()).setTitle("Error")
                .setMessage("The selected tag does not have a valid value type")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        ad.show();
    }

    /**
     * Show a dialog to get a String value from the user
     *
     * @param tag name of the tag
     */
    void showTextDialog(final String tag) {
        final EditText input = new EditText(getActivity());
        input.setScaleX(0.9f);
        input.setTextColor(getResources().getColor(android.R.color.black));

        AlertDialog ad = new AlertDialog.Builder(getActivity()).setView(input).setTitle(getString(R.string.enter_value) + tag)
                .setCancelable(false)
                .setPositiveButton(getActivity().getApplicationContext().getString(R.string.add_value_button), new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        assignValueToTag(tag, input.getText().toString());
                    }
                })
                .create();
        ad.setCanceledOnTouchOutside(true);
        ad.show();
    }

    /**
     * Show a dialog to get a float value from the user
     *
     * @param tag name of the tag
     */
    void showFloatDialog(final String tag) {
        final EditText input = new EditText(getActivity());
        input.setScaleX(0.9f);
        input.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        input.setTextColor(getResources().getColor(android.R.color.black));

        AlertDialog ad = new AlertDialog.Builder(getActivity()).setView(input).setTitle(getString(R.string.enter_value) + tag)
                .setCancelable(false)
                .setPositiveButton(getActivity().getApplicationContext().getString(R.string.add_value_button), new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        assignValueToTag(tag, input.getText().toString());
                    }
                })
                .create();
        ad.setCanceledOnTouchOutside(true);
        ad.show();
    }

    /**
     * Show a dialog to get a integer value from the user
     *
     * @param tag name of the tag
     */
    void showIntegerDialog(final String tag) {
        final EditText input = new EditText(getActivity());
        input.setScaleX(0.9f);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setTextColor(getResources().getColor(android.R.color.black));

        AlertDialog ad = new AlertDialog.Builder(getActivity()).setView(input).setTitle(getString(R.string.enter_value) + tag)
                .setCancelable(false)
                .setPositiveButton(getActivity().getApplicationContext().getString(R.string.add_value_button), new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        assignValueToTag(tag, input.getText().toString());
                    }
                })
                .create();
        ad.setCanceledOnTouchOutside(true);
        ad.show();
    }

    /**
     * Show a dialog to get a boolean value from the user
     *
     * @param tag name of the tag
     */
    void showBooleanDialog(final String tag) {
        AlertDialog ad = new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.enter_value) + " " + tag)

                .setPositiveButton(getActivity().getString(R.string.boolean_value_true),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int buttonId) {
                                dialog.dismiss();
                                assignValueToTag(tag, "true");
                            }
                        })
                .setNegativeButton(getActivity().getString(R.string.boolean_value_false),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int buttonId) {
                                dialog.dismiss();
                                assignValueToTag(tag, "false");
                            }
                        }).create();
        ad.setCanceledOnTouchOutside(true);
        ad.show();
    }
}