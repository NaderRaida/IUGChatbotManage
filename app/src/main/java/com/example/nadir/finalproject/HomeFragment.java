package com.example.nadir.finalproject;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

///////////////////////////


import android.os.AsyncTask;
import android.widget.Toast;

import com.ibm.cloud.sdk.core.http.Response;
import com.ibm.cloud.sdk.core.service.security.IamOptions;
import com.ibm.watson.assistant.v1.Assistant;
import com.ibm.watson.assistant.v1.model.CreateIntentOptions;
import com.ibm.watson.assistant.v1.model.Example;
import com.ibm.watson.developer_cloud.conversation.v1.model.Intent;
import com.ibm.watson.developer_cloud.http.ServiceCall;


import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;



public class HomeFragment extends Fragment {
    Spinner spinner;
    TextView intent_name_label,intent_example_label,intent_description_label,node_name_label,node_condition_label,node_title_label,node_output_label;
    EditText intent_name_et,intent_example_et,intent_description_et,node_name_et,node_condition_et,node_title_et,node_output_et;
    Button addToListBtn,doneDtn;
    List<Example> intentExamplesList;
    IamOptions iamOptions;
    Assistant service;
    String workspaceId;
    BufferedReader reader;
    String firstLine;
    List<String> questionsList = new ArrayList<>();
    List<String> answersList = new ArrayList<>();
    /*public static int isSubstring(String str, String pattern)
    {
        int str_length = str.length();
        int pattern_length = pattern.length();

        for (int i = 0; i <= str_length - pattern_length; i++)
        {
            int j;

            for (j = 0; j < pattern_length; j++)
                if (str.charAt(i + j) != pattern.charAt(j))
                    break;

            if (j == pattern_length)
                return i;
        }
        return -1;
    }
    public  String deleteAll(String str, String pattern)
    {
        for(int index = isSubstring(str, pattern); index != -1; index = isSubstring(str, pattern))
            str = deleteSubstring(str, pattern, index);

        return str;
    }
    public static String deleteSubstring(String str, String pattern, int index)
    {
        int start_index = index;
        int end_index = start_index + pattern.length() - 1;
        int dest_index = 0;
        char[] result = new char[str.length()];


        for(int i = 0; i< str.length() - 1; i++)
            if(i < start_index || i > end_index)
                result[dest_index++] = str.charAt(i);

        return new String(result, 0, dest_index + 1);
    }
    */
    private void readTextFromFile(String fileTextName){
        InputStream inputStream = null;
        try {
            inputStream= getActivity().getAssets().open(fileTextName);

            reader = new BufferedReader(new InputStreamReader(inputStream));
            String question;
            //read first line only
            String line = reader.readLine();
            question = line.trim().substring(2,(line.length()-1)).trim();
            questionsList.add(question);
            while(line != null){
                Log.d("Full Line", line);
                line = reader.readLine();
                if(line != null){
                    if (line.startsWith("س:")){
                        question = line.trim().substring(2,(line.length()-1)).trim();
                        questionsList.add(question);
                    }else if(line.startsWith("ج:")){
                        String answer = line.trim().substring(2,(line.length()-1));
                        answersList.add(answer);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();

        }finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connection();
        intentExamplesList = new ArrayList<Example>();
        readTextFromFile("questions.txt");

        for (int i = 0; i <questionsList.size() ; i++) {
            Log.d("Question : "+i,questionsList.get(i));
        }
        for (int i = 0; i <answersList.size() ; i++) {
            Log.d("Answer : "+i,answersList.get(i));
        }
    }

    public void connection(){
        iamOptions = new IamOptions.Builder().apiKey("NuOq_3z2n7v2KDI4Y4PQVy0l6B9i7KD850btpKYdZRt9").build();
        service= new Assistant("2016-07-11", iamOptions);
        service.setEndPoint("https://gateway-lon.watsonplatform.net/assistant/api");
        workspaceId = "39e99aca-bd78-4af8-94d7-8f28390371f3";
    }
    public ServiceCall<com.ibm.watson.developer_cloud.conversation.v1.model.Intent> createIntent(){


        String intent_name = intent_name_et.getText().toString();



        CreateIntentOptions options = new CreateIntentOptions.Builder(workspaceId, intent_name)
                .examples(intentExamplesList)
                .description(intent_description_et.getText().toString())
                .build();
        Response<com.ibm.watson.assistant.v1.model.Intent> response = service.createIntent(options).execute();
        return null;
    }
    private class createIntentOperation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            createIntent();


            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getActivity().getApplicationContext(), "Do in Background", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("key",spinner.getSelectedItemPosition());

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable  Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.fragment_home, container, false);
        spinner =  v.findViewById(R.id.spinner);
        intent_name_label = v.findViewById(R.id.label_name);
        intent_example_label = v.findViewById(R.id.label_example);
        intent_name_et = v.findViewById(R.id.name_et);
        intent_example_et = v.findViewById(R.id.example_et);
        intent_description_label = v.findViewById(R.id.label_description);
        intent_description_et = v.findViewById(R.id.description_et);
        node_name_label = v.findViewById(R.id.label_name1);
        node_condition_label = v.findViewById(R.id.label_condition);
        node_name_et = v.findViewById(R.id.name_et1);
        node_condition_et = v.findViewById(R.id.condition_et);
        node_title_label = v.findViewById(R.id.label_title);
        node_title_et = v.findViewById(R.id.title_et);
        node_output_label = v.findViewById(R.id.label_output);
        node_output_et = v.findViewById(R.id.output_et);
        addToListBtn = v.findViewById(R.id.addToListBtn);
        doneDtn = v.findViewById(R.id.doneBtn);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity().getApplicationContext(),
                R.array.operations, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        addToListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentExamplesList.add(new Example.Builder(intent_example_et.getText().toString()).build());
            }
        });
        doneDtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new createIntentOperation().execute("");
                Toast.makeText(getActivity().getApplicationContext(), "done click", Toast.LENGTH_SHORT).show();
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 0 :
                        showIntentsOptions();
                        break;
                    case 1:
                        showEntitiesOptions();
                        break;
                    case 2:
                        showDialogOptions();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return v;

    }
    public void intentOperations(){

    }
    public void showIntentsOptions(){
        intent_name_et.setVisibility(View.VISIBLE);
        intent_name_label.setVisibility(View.VISIBLE);
        intent_example_et.setVisibility(View.VISIBLE);
        intent_example_label.setVisibility(View.VISIBLE);
        intent_description_label.setVisibility(View.VISIBLE);
        intent_description_et.setVisibility(View.VISIBLE);
        node_name_et.setVisibility(View.GONE);
        node_name_label.setVisibility(View.GONE);
        node_condition_et.setVisibility(View.GONE);
        node_condition_label.setVisibility(View.GONE);
        node_title_label.setVisibility(View.GONE);
        node_title_et.setVisibility(View.GONE);
        node_output_label.setVisibility(View.GONE);
        node_output_et.setVisibility(View.GONE);
    }
    public void showDialogOptions(){
        intent_name_et.setVisibility(View.GONE);
        intent_name_label.setVisibility(View.GONE);
        intent_example_et.setVisibility(View.GONE);
        intent_example_label.setVisibility(View.GONE);
        intent_description_label.setVisibility(View.GONE);
        intent_description_et.setVisibility(View.GONE);
        node_name_et.setVisibility(View.VISIBLE);
        node_name_label.setVisibility(View.VISIBLE);
        node_condition_et.setVisibility(View.VISIBLE);
        node_condition_label.setVisibility(View.VISIBLE);
        node_title_label.setVisibility(View.VISIBLE);
        node_title_et.setVisibility(View.VISIBLE);
        node_output_label.setVisibility(View.VISIBLE);
        node_output_et.setVisibility(View.VISIBLE);
    }
    public void showEntitiesOptions(){
        intent_name_et.setVisibility(View.GONE);
        intent_name_label.setVisibility(View.GONE);
        intent_example_et.setVisibility(View.GONE);
        intent_example_label.setVisibility(View.GONE);
        intent_description_label.setVisibility(View.GONE);
        intent_description_et.setVisibility(View.GONE);
        node_name_et.setVisibility(View.GONE);
        node_name_label.setVisibility(View.GONE);
        node_condition_et.setVisibility(View.GONE);
        node_condition_label.setVisibility(View.GONE);
        node_title_label.setVisibility(View.GONE);
        node_title_et.setVisibility(View.GONE);
        node_output_label.setVisibility(View.GONE);
        node_output_et.setVisibility(View.GONE);
    }


}