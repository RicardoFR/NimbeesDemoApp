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
 *
 * This TagFragment lets see all the tags that belongs to your app
 * and the values that you have defined.
 *
 * Say your preferences to the bees!
 */

public class TagFragment extends Fragment {

    /** Index to remember the position of the selected item in the list.*/
    private int INDEX = 0;

    /**
     * Spinner element to show all Tags.
     */
    private Spinner mSpinner;

    /**
     * Wheel to show load process
     */
    private ProgressWheel mWheel;

    /**
     * Button to get Tags
     */
    private ButtonRectangle mButtonGetTags;

    /**
     * Button to add a Value to a Tag
     */
    private ButtonRectangle mButtonAddValue;

    /**
     * RecyclerView to be used with the values of Tags.
     */
    private RecyclerView mRecyclerView;

    /**
     * LinearLayout to be used with the RecyclerView.
     */
    private LinearLayoutManager mLayoutManager;

    /**
     * List of Tags name
     */
    private ArrayList<String> mItemsName;

    /**
     * List of Tags elements
     */
    private List<NimbeesTag> mTagList;

    /**
     * List of values of each TAG
     */
    private List<String> mTagValues;

    /**
     * ArrayAdapter to show Tags names in the spinner.
     */
    private ArrayAdapter<String> mAdapter;

    /**
     * Adapter to show values of tags in UI.
     */
    private ValuesAdapter mItemsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTagValues = new ArrayList<String>();
        mItemsName = new ArrayList<String>();

        //Call to the getTags() to populate the items list using the adapter
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

        // Adapter and list of Tags
        mRecyclerView = (RecyclerView) view.findViewById(R.id.tag_RecyclerView);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mItemsAdapter = new ValuesAdapter(mItemsName);
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
                                    removeValueFromTag(mTagValues.get(INDEX).toString(), mItemsName.get(position));
                                    mItemsName.remove(position);
                                }
                                mItemsAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    removeValueFromTag(mTagValues.get(INDEX).toString(), mItemsName.get(position));
                                    mItemsName.remove(position);
                                }
                                mItemsAdapter.notifyDataSetChanged();
                            }

                        });

        mRecyclerView.addOnItemTouchListener(swipeTouchListener);

        // Listener of the spinner to do actions on a item selected
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            // To do actions when a item from the Spinner is selected
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                mWheel.setVisibility(View.VISIBLE);
                loadDatafromTag(mTagValues.get(i).toString());
                INDEX = i;

            }

            // Default action when no element in the list is selected
            public void onNothingSelected(AdapterView<?> arg0) {
            }

        });

        // Button to add a value to a TAG
        mButtonAddValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog(mTagValues.get(INDEX).toString());

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

        mItemsName.clear();
        for (String s : stringList) {
            mItemsName.add(s);
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

                setElementsOnSpinner(tagBeans);
                mWheel.setVisibility(View.INVISIBLE);
                Toast.makeText(getActivity().getApplicationContext(), "Tags recovered : " + String.valueOf(tagBeans.size()), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(NimbeesException e) {
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
                setValuesOnList(strings);
                mWheel.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(NimbeesException e) {
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
                Toast.makeText(getActivity().getApplicationContext(), "Value added", Toast.LENGTH_LONG).show();
                loadDatafromTag(tagname);
            }

            @Override
            public void onFailure(NimbeesException e) {
            }
        });
    }

    /**
     * Remove a value from a Tag
     *
     * @param tagname  the name of the tag
     * @param tagvalue the value to remove from the tag
     */
    private void removeValueFromTag(final String tagname, String tagvalue) {

        NimbeesClient.getTagManager().deleteServerTagValue(tagname, tagvalue, new NimbeesCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                Toast.makeText(getActivity().getApplicationContext(), "Value removed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(NimbeesException e) {

            }
        });
    }

    /**
     * Select a proper dialog to show it based on tag type
     *
     * @param tag name of the tag
     */
    private void showDialog(final String tag) {

        String valuetype = "";

        for (NimbeesTag nt : mTagList) {

            if (tag == nt.getName()) {
                valuetype = nt.getValueType();
                continue;
            }
        }
        switch (valuetype) {
            case "TEXT":
                showTextDialog(tag);
                break;
            case "FLOAT":
                showFloatDialog(tag);
                break;
            case "INTEGER":
                showIntegerDialog(tag);
                break;
            case "BOOLEAN":
                showBooleanDialog(tag);
                break;
        }
    }

    /**
     * Show a dialog to get a String value from the user
     *
     * @param tag name of the tag
     */
     void showTextDialog(final String tag) {

        AlertDialog ad = new AlertDialog.Builder(getActivity()).create();
        final EditText input = new EditText(getActivity());
        input.setScaleX(0.9f);
        ad.setView(input);

        ad.setTitle(getString(R.string.enter_value) + tag);
        ad.setCancelable(false);
        ad.setButton(getActivity().getApplicationContext().getString(R.string.add_value_button), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                assignValueToTag(tag, input.getText().toString());
            }
        });
        ad.setCanceledOnTouchOutside(true);
        ad.show();
    }

    /**
     * Show a dialog to get a float value from the user
     *
     * @param tag name of the tag
     */
     void showFloatDialog(final String tag) {

        AlertDialog ad = new AlertDialog.Builder(getActivity()).create();
        final EditText input = new EditText(getActivity());
        input.setScaleX(0.9f);
        input.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        ad.setView(input);
        ad.setTitle(getString(R.string.enter_value) + tag);
        ad.setCancelable(false);
        ad.setButton(getActivity().getApplicationContext().getString(R.string.add_value_button), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                assignValueToTag(tag, input.getText().toString());
            }
        });
        ad.setCanceledOnTouchOutside(true);
        ad.show();
    }

    /**
     * Show a dialog to get a integer value from the user
     *
     * @param tag name of the tag
     */
     void showIntegerDialog(final String tag) {

        AlertDialog ad = new AlertDialog.Builder(getActivity()).create();
        final EditText input = new EditText(getActivity());
        input.setScaleX(0.9f);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        ad.setView(input);
        ad.setTitle(getString(R.string.enter_value) + tag);
        ad.setCancelable(false);
        ad.setButton(getActivity().getApplicationContext().getString(R.string.add_value_button), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                assignValueToTag(tag, input.getText().toString());
            }
        });
        ad.setCanceledOnTouchOutside(true);
        ad.show();
    }

    /**
     * Show a dialog to get a boolean value from the user
     *
     * @param tag name of the tag
     */
     void showBooleanDialog(final String tag) {

        AlertDialog ad = new AlertDialog.Builder(getActivity()).create();
        ad.setTitle(getString(R.string.enter_value) + " " + tag);

        ad.setButton(DialogInterface.BUTTON_POSITIVE, "True",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int buttonId) {
                        dialog.dismiss();
                        assignValueToTag(tag, "true");
                    }
                });
        ad.setButton(DialogInterface.BUTTON_NEGATIVE, "False",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int buttonId) {
                        dialog.dismiss();
                        assignValueToTag(tag, "false");
                    }
                });
        ad.setCanceledOnTouchOutside(true);
        ad.show();
    }
}