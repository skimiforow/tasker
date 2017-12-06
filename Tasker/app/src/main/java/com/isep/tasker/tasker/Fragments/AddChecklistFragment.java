package com.isep.tasker.tasker.Fragments;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.isep.tasker.tasker.Adapters.CheckListAdapter;
import com.isep.tasker.tasker.Domain.Items;
import com.isep.tasker.tasker.Domain.Priority;
import com.isep.tasker.tasker.Domain.State;
import com.isep.tasker.tasker.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddChecklistFragment extends Fragment {

    private ListView lw;
    private ArrayList<Items> items;
    private Button btnAddItem;
    private Spinner importance;
    private EditText text;
    private CheckListAdapter checkListAdapter;

    public AddChecklistFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate ( R.layout.fragment_add_checklist, container, false );

        items = new ArrayList<> (  );
        lw = (ListView) inflate.findViewById(R.id.lstChecklist);
        checkListAdapter = new CheckListAdapter ( getContext (),R.layout.checklist_item ,items );
        lw.setAdapter( checkListAdapter);
        btnAddItem = inflate.findViewById ( R.id.addItem );





        return inflate;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu ( menu, v, menuInfo );
        MenuInflater menuInflater = getActivity ().getMenuInflater ();
        menuInflater.inflate ( R.menu.menu_checklist ,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)  item.getMenuInfo ();
        int index = info.position;
        View view = info.targetView;
        switch (item.getItemId ()){
            case R.id.edit:
                EditDialog( (Items) lw.getItemAtPosition ( item.getItemId () ) );
                return true;
            case R.id.remove:
                return true;
        }
        return super.onContextItemSelected ( item );
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated ( view, savedInstanceState );
        btnAddItem.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick(View view) {
                // custom dialog
                final Dialog dialog = new Dialog(getContext ());
                dialog.setContentView(R.layout.checklist_popup_screen);
                dialog.setTitle(getString (R.string.add_checklist));
                text = (EditText) dialog.findViewById(R.id.editText_description);
                importance = (Spinner) dialog.findViewById(R.id.spnImportance);
                ArrayAdapter<Priority> spinnerAdapter = new ArrayAdapter<Priority> ( getContext (),R.layout.spinner_full,Priority.values () );
                importance.setAdapter(spinnerAdapter);
                importance.setSelection ( 1,true );
                Button dialogButton = (Button) dialog.findViewById(R.id.submeter);
                dialogButton.setOnClickListener ( new View.OnClickListener ( ) {
                    @Override
                    public void onClick(View view) {
                        Items item = new Items (  );
                        item.setDescription (  text.getText ().toString ());
                        item.setPriority((Priority) importance.getSelectedItem ( ));
                        items.add ( item );
                        checkListAdapter.notifyDataSetChanged ();
                        dialog.hide ();
                    }
                } );

                dialog.show();
            }
        } );

    }

    public void EditDialog(Items item) {
        // custom dialog
        final Dialog dialog = new Dialog(getContext ());
        dialog.setContentView(R.layout.checklist_popup_screen);
        dialog.setTitle(getString (R.string.add_checklist));
        text = (EditText) dialog.findViewById(R.id.editText_description);
        text.setText ( item.getDescription () );
        importance = (Spinner) dialog.findViewById(R.id.spnImportance);
        ArrayAdapter<Priority> spinnerAdapter = new ArrayAdapter<Priority> ( getContext (),R.layout.spinner_full,Priority.values () );
        importance.setAdapter(spinnerAdapter);
        importance.setSelection ( item.getPriority ().ordinal (),true );
        Button dialogButton = (Button) dialog.findViewById(R.id.submeter);
        dialogButton.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick(View view) {
                Items item = new Items (  );
                item.setDescription (  text.getText ().toString ());
                item.setPriority((Priority) importance.getSelectedItem ( ));
                items.add ( item );
                checkListAdapter.notifyDataSetChanged ();
                dialog.hide ();
            }
        } );

        dialog.show();
    }

}
