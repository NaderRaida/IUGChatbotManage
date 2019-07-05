package com.example.nadir.finalproject;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

///////////////////////////


import android.os.AsyncTask;
import android.widget.Toast;

import com.example.nadir.finalproject.arabicstemmer.ArabicStemmer;
import com.ibm.cloud.sdk.core.http.Response;
import com.ibm.cloud.sdk.core.service.security.IamOptions;
import com.ibm.watson.assistant.v1.Assistant;
import com.ibm.watson.assistant.v1.model.CreateDialogNodeOptions;
import com.ibm.watson.assistant.v1.model.CreateIntentOptions;
import com.ibm.watson.assistant.v1.model.DialogNode;
import com.ibm.watson.assistant.v1.model.DialogNodeOutput;
import com.ibm.watson.assistant.v1.model.DialogNodeOutputGeneric;
import com.ibm.watson.assistant.v1.model.DialogNodeOutputTextValuesElement;
import com.ibm.watson.assistant.v1.model.Example;
import com.ibm.watson.developer_cloud.http.ServiceCall;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;


public class HomeFragment extends Fragment {
    Spinner spinner;
    TextView intent_name_label,intent_example_label,intent_description_label,node_name_label,node_condition_label,node_title_label,node_output_label;
    EditText intent_name_et,intent_example_et,intent_description_et,node_name_et,node_condition_et,node_title_et,node_output_et;
    Button addToListBtn, feedButton;
    List<Example> intentExamplesList;
    IamOptions iamOptions;
    Assistant service;
    String workspaceId;
    BufferedReader reader;
    List<String> questionsList = new ArrayList<>();
    List<String> answersList = new ArrayList<>();
    List<String> stopWordsList = new ArrayList<>();
    List<Question> questionObjectsList = new ArrayList<>();

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
    private List<String> removeStopWordsAndStemming(List<String> targetQuestionsList,List<String> stopWordsList,String fileStopWordsName){
        InputStream inputStream = null;
        try {
            inputStream= getActivity().getAssets().open(fileStopWordsName);
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String stopWord;
            String line = reader.readLine(); stopWord = line.trim();
            stopWordsList.add(stopWord);
            while(line != null){
                line = reader.readLine();
                if(line != null){
                    stopWord = line;
                    stopWordsList.add(stopWord);
                } }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            } }
        List<String> clearQuestions = new ArrayList<>();
        List<String> wordsList =null;
        for (int i = 0; i < targetQuestionsList.size(); i++) {
            String question = targetQuestionsList.get(i);
            wordsList = convertQuestionToWords(question,targetQuestionsList.size());
            for (int j = 0; j < stopWordsList.size(); j++) {
                if (wordsList.contains(stopWordsList.get(j))) {
                    wordsList.remove(stopWordsList.get(j));
                }
            }
            StringBuilder sb =new StringBuilder();
            String afterStemming ;
            for (int t = 0; t < wordsList.size(); t++) {
                afterStemming = stemmingWords(wordsList.get(t));
                sb.append(afterStemming+" ");
            }
            clearQuestions.add(sb.toString());
        }
        return clearQuestions;
    }
    private List<String> convertQuestionToWords(String question,int size){
        String[] words = null ;
        List<String> wordsList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            String clean = question.trim().replaceAll("\\s+", " ");
             words = clean.split(" ");
        }
        for (String word : words) {
            wordsList.add(word);
        }

        return wordsList;
    }
    private void readTextFromFile(String fileTextName){
        InputStream inputStream = null;
        try {
            inputStream= getActivity().getAssets().open(fileTextName);
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String question;
            String line = reader.readLine();
            question = line.trim().substring(3,(line.length()-1)).trim();
            questionsList.add(question);
            while(line != null){
                line = reader.readLine();
                if(line != null){
                    if (line.startsWith("س:")){
                        question = line.trim().substring(3,(line.length()-1)).trim();
                        questionsList.add(question);
                    }else if(line.startsWith("ج:")){
                        String answer = line.trim().substring(2,(line.length()-1)).replaceAll("#","");
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
    private List<Question> createQuestionObjects
            (List<String> questionsList,List<String> answersList,List<String> clearQuestionList){
        List<Question> list= new ArrayList<>();
        for (int i = 0; i < questionsList.size(); i++) {
            String [] questionWords =clearQuestionList.get(i).split(" ");
            ArrayList<String> arrayListOfQuestionWords = new ArrayList<>(Arrays.asList(questionWords));
            list.add(new Question(arrayListOfQuestionWords,questionsList.get(i),answersList.get(i)));
        }
        return list;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        List<String> listA = stemming(answersList);
        /*
        for (int i = 0; i <questionsList.size() ; i++) {
            Log.d("Question : "+i,questionsList.get(i));
        }
        for (int i = 0; i <answersList.size() ; i++) {
            Log.d("Answer : "+i,answersList.get(i));
        }
        for (int i = 0; i < stopWordsList.size(); i++) {
//            Log.d("size of list",stopWordsList.size()+"");
            Log.d("stopWord Content",stopWordsList.get(i));
        }
        */
//        ArabicStemmer stemmer = new ArabicStemmer(getContext().getAssets());
//////
//        Log.d("234",stemmer.stemWord("العروبة"));




//        for (int i = 0; i < questionObjectsList.size(); i++) {
//            for (int j = 0; j < questionObjectsList.get(1).getListOfWords().size(); j++) {
//                Log.d("laaal",questionObjectsList.get(1).getListOfWords().get(j));
//            }
//        }

//        Log.d("asd",UUID.randomUUID().toString());

        String key = "x_"+new Random().nextInt(1000000);
    }
    private List<String> stemming(List<String> targetList){
        List<String> clearList = new ArrayList<>();
        List<String> wordsList =null;
        //get strings from lit and split it and add it to temp list from words > and loop on it
        // and compare it with words of stop words list
        for (int i = 0; i < targetList.size(); i++) {
            String line = targetList.get(i);
            wordsList = convertQuestionToWords(line,targetList.size());

            StringBuilder sb =new StringBuilder();

            for (int t = 0; t < wordsList.size(); t++) {
                String afterStemming = stemmingWords(wordsList.get(t));
                sb.append(afterStemming+" ");
            }
            clearList.add(sb.toString());
        }
        return  clearList;
    }
    private String stemmingWords(String word){
        ArabicStemmer arabicStemmer = new ArabicStemmer();
        arabicStemmer.setCurrent(word);
        arabicStemmer.stem();
        return arabicStemmer.getCurrent();
    }

    public void connection(){
        iamOptions = new IamOptions.Builder().apiKey("NuOq_3z2n7v2KDI4Y4PQVy0l6B9i7KD850btpKYdZRt9").build();
        service= new Assistant("2016-07-11", iamOptions);
        service.setEndPoint("https://gateway-lon.watsonplatform.net/assistant/api");
        workspaceId = "39e99aca-bd78-4af8-94d7-8f28390371f3";
    }
    private class CreateIntentAndNodeOperation extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            createIntentandNode(questionObjectsList);
            return "Executed";
        }

        @Override
        protected void onPostExecute(String s) {
            tellFinishAdd();
        }
    }
    private void tellFinishAdd(){
        Toast.makeText(getActivity().getApplicationContext(), "Adding intent and nodes finished!", Toast.LENGTH_SHORT).show();
    }
    public ServiceCall<DialogNode> createDialogNode(String workspaceId,Assistant service,String intent_name,String answer){
        String random = "X_"+new Random().nextInt(1000000);
        String dialogNode = random;
        String conditions = "#"+intent_name;
        String title =random ;
        DialogNodeOutput dialogNodeOutput = new DialogNodeOutput();
        List<DialogNodeOutputGeneric> nodeOutputGenericList = new ArrayList<>();
        List<DialogNodeOutputTextValuesElement> textValuesElementList = new ArrayList<>();
        DialogNodeOutputGeneric nodeOutputGeneric = new DialogNodeOutputGeneric();
        DialogNodeOutputTextValuesElement nodeOutputTextValuesElement = new DialogNodeOutputTextValuesElement();
        nodeOutputTextValuesElement.setText(answer);
        nodeOutputGeneric.setResponseType(DialogNodeOutputGeneric.ResponseType.TEXT);
        textValuesElementList.add(nodeOutputTextValuesElement);
        nodeOutputGeneric.setValues(textValuesElementList);
        nodeOutputGenericList.add(nodeOutputGeneric);
        dialogNodeOutput.setGeneric(nodeOutputGenericList);
        CreateDialogNodeOptions optionsNode = new CreateDialogNodeOptions.Builder(workspaceId, dialogNode)
                .conditions(conditions)
                .title(title)
                .output(dialogNodeOutput)
                .build();

        DialogNode responseNode = service.createDialogNode(optionsNode).execute().getResult();
        return null;
    }
    public ServiceCall<com.ibm.watson.developer_cloud.conversation.v1.model.Intent> createIntentandNode(List<Question> list){

        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.get(i).getListOfWords().size(); j++) {
                int found = 0;
                for (int k = 0; k < intentExamplesList.size(); k++) {
                    if(intentExamplesList.get(k).text().equalsIgnoreCase(list.get(i).getListOfWords().get(j))){
                        found++;
                    }
                }
                if(found == 0 && list.get(i).getListOfWords().get(j).length()>0){
                    intentExamplesList.add(
                            new Example.Builder(list.get(i).getListOfWords().get(j)).build()
                    );
                }
            }
            String intent_name = UUID.randomUUID().toString();
            CreateIntentOptions options = new CreateIntentOptions.Builder(workspaceId, intent_name)
                    .examples(intentExamplesList)
                    .build();
            Response<com.ibm.watson.assistant.v1.model.Intent> response = service.createIntent(options).execute();
            createDialogNode(workspaceId,service,intent_name,list.get(i).getAnswerText());
            intentExamplesList.clear();
        }

        return null;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable  Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.fragment_home, container, false);
        feedButton = v.findViewById(R.id.doneBtn);

        feedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity().getApplicationContext(), "Adding intent and nodes finished!", Toast.LENGTH_SHORT).show();
                    connection();
                    intentExamplesList = new ArrayList<Example>();
                    readTextFromFile("unicodefqa.txt");
                    List<String> listQ = removeStopWordsAndStemming(questionsList,stopWordsList, "stopwords.txt");
//                    for (int i = 0; i < listQ.size(); i++) {
//                        Log.d("Question after remove stop words and stemming: "+i,listQ.get(i));
//
//                    }
                    questionObjectsList = createQuestionObjects(questionsList,answersList,listQ);

                    new CreateIntentAndNodeOperation().execute("");


            }
        });

        return v;

    }


}