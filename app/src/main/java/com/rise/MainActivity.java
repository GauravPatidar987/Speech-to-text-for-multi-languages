package com.rise;

import android.app.*;
import android.os.*;
import android.widget.*;
import android.view.*;
import android.content.*;
import android.speech.*;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.*;
import androidx.activity.result.*;
import java.util.*;
import androidx.appcompat.app.*;
public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener
{

	@Override
	public void onItemSelected(AdapterView<?> p1, View p2, int p3, long p4)
	{
		
		lcode = lCodes[p3];
	}

	@Override
	public void onNothingSelected(AdapterView<?> p1)
	{
		// TODO: Implement this method
	}

	EditText etText;
    ImageView ivMic,ivCopy;
    Spinner spLangs;
    String lcode = "en-US";

    // Languages included
    String[] languages = {"English","Tamil","Hindi","Spanish","French",
		"Arabic","Chinese","Japanese","German"};

    // Language codes
    String[] lCodes = {"en-US","ta-IN","hi-IN","es-CL","fr-FR",
		"ar-SA","zh-TW","jp-JP","de-DE"};

	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		etText = findViewById(R.id.etSpeech);
        ivMic = findViewById(R.id.ivSpeak);
        ivCopy = findViewById(R.id.ivCopy);
        spLangs = findViewById(R.id.spLang);

        // set onSelectedItemListener for the spinner
        spLangs.setOnItemSelectedListener(this);

        // setting array adapter for spinner
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,languages);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spLangs.setAdapter(adapter);
		
		final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
			new ActivityResultContracts.StartActivityForResult(),
			new ActivityResultCallback<ActivityResult>() {
				@Override
				public void onActivityResult(ActivityResult result) {
					// if result is not empty
					if (result.getResultCode() == Activity.RESULT_OK && result.getData()!=null) {
						// get data and append it to editText
						ArrayList<String> d=result.getData().getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
						etText.setText(etText.getText()+" "+d.get(0));
					}
				}
			});

        // on click listener for mic icon
        ivMic.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					// creating intent using RecognizerIntent to convert speech to text
					Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
					intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
					intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,lcode);
					intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Speak now!");
					// starting intent for result
					activityResultLauncher.launch(intent);
				}
			});
		ivCopy.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					// code to copy to clipboard
					ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
					clipboardManager.setPrimaryClip(ClipData.newPlainText("label",etText.getText().toString().trim()));
					Toast.makeText(MainActivity.this, "Copied!", Toast.LENGTH_SHORT).show();
				}
			});
    
	
}
    
}
