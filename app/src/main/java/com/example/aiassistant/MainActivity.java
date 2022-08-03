package com.example.aiassistant;

import static com.example.ai_webza_tec.ai_method.checkForPreviousCallList;
import static com.example.ai_webza_tec.ai_method.clearContactListSavedData;
import static com.example.ai_webza_tec.ai_method.getContactList;
import static com.example.ai_webza_tec.ai_method.makeCall;
import static com.example.ai_webza_tec.ai_method.makeCallFromSavedContactList;
import static com.example.aiassistant.Functions.fetchName;
import static com.example.aiassistant.Functions.wishMe;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private SpeechRecognizer recognizer;
    private TextView tvResult;
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Dexter.withContext(this)
                .withPermission(Manifest.permission.RECORD_AUDIO)
                .withListener(new PermissionListener() {
                    @Override public void onPermissionGranted(PermissionGrantedResponse response) {}
                    @Override public void onPermissionDenied(PermissionDeniedResponse response) {
                        System.exit(0);
                    }
                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();

        findViewById();
        initializeTextToSpeech();
        initializeResult();
    }

    private void initializeTextToSpeech() {
        tts=new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(tts.getEngines().size()==0)
                    Toast.makeText(MainActivity.this, "Engine is not available", Toast.LENGTH_SHORT).show();
                else{
                    String s=wishMe();
                    speak(s);
                }
            }
        });
    }

    private void speak(String msg) {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP)
            tts.speak(msg,TextToSpeech.QUEUE_FLUSH,null,null);
        else
            tts.speak(msg,TextToSpeech.QUEUE_FLUSH,null);
    }

    private void findViewById() {
        tvResult=(TextView) findViewById(R.id.tv_result);
    }

    private void initializeResult() {
        if(SpeechRecognizer.isRecognitionAvailable(this)) {
            recognizer=SpeechRecognizer.createSpeechRecognizer(this);
            recognizer.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle bundle) {

                }

                @Override
                public void onBeginningOfSpeech() {

                }

                @Override
                public void onRmsChanged(float v) {

                }

                @Override
                public void onBufferReceived(byte[] bytes) {

                }

                @Override
                public void onEndOfSpeech() {

                }

                @Override
                public void onError(int i) {

                }

                @Override
                public void onResults(Bundle bundle) {
                    ArrayList<String> result=bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    tvResult.setText(result.get(0));
                    response(result.get(0));
                }

                @Override
                public void onPartialResults(Bundle bundle) {

                }

                @Override
                public void onEvent(int i, Bundle bundle) {

                }
            });
        }
    }

    private void response(String msg) {
        String msgs=msg.toLowerCase();

        if(msgs.indexOf("hi")!=-1 || msgs.indexOf("hello")!=-1)
            speak("Hello Sir ! How are you ?");

        if(msgs.indexOf("i am fine")!=-1)
            speak("it's good to know that you are fine...How may I help you ?");
        else if(msgs.indexOf("i am not fine")!=-1)
            speak("Please take care Sir");

        if(msgs.indexOf("what")!=-1) {

            if(msgs.indexOf("your")!=-1) {
                if(msgs.indexOf("name")!=-1) {
                    speak("My name is Friday and my processor is 3007 Mark 1");
                }
            }

            if(msgs.indexOf("time")!=-1) {
                if(msgs.indexOf("now")!=-1) {
                    Date date=new Date();
                    String time=DateUtils.formatDateTime(this,date.getTime(),DateUtils.FORMAT_SHOW_TIME);
                    speak("The time now is "+time);
                }
            }

            if(msgs.indexOf("today")!=-1) {
                if(msgs.indexOf("date")!=-1) {
                    SimpleDateFormat df=new SimpleDateFormat("dd MM yyyy");
                    Calendar cal=Calendar.getInstance();
                    String todayDate=df.format(cal.getTime());
                    speak("The Today's date is "+todayDate);
                }
            }
        }

        if(msgs.indexOf("open")!=-1) {

            if(msgs.indexOf("google")!=-1) {
                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com"));
                startActivity(intent);
            }

            if(msgs.indexOf("browser")!=-1) {
                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com"));
                startActivity(intent);
            }

            if(msgs.indexOf("chrome")!=-1) {
                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com"));
                startActivity(intent);
            }

            if(msgs.indexOf("youtube")!=-1) {
                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com"));
                startActivity(intent);
            }

            if(msgs.indexOf("facebook")!=-1) {
                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com"));
                startActivity(intent);
            }

            if(msgs.indexOf("whatsapp")!=-1) {
                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.whatsapp.com"));
                startActivity(intent);
            }

            if(msgs.indexOf("instagram")!=-1) {
                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com"));
                startActivity(intent);
            }

            if(msgs.indexOf("telegram")!=-1) {
                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.telegram.com"));
                startActivity(intent);
            }

            if(msgs.indexOf("discord")!=-1) {
                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.discord.com"));
                startActivity(intent);
            }

            if(msgs.indexOf("snapchat")!=-1) {
                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.snapchat.com"));
                startActivity(intent);
            }

            if(msgs.indexOf("messenger")!=-1) {
                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.messenger.com"));
                startActivity(intent);
            }

            if(msgs.indexOf("spotify")!=-1) {
                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.spotify.com"));
                startActivity(intent);
            }

            if(msgs.indexOf("linkedin")!=-1) {
                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com"));
                startActivity(intent);
            }

            if(msgs.indexOf("email")!=-1) {
                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.gmail.com"));
                startActivity(intent);
            }
        }

        if(msgs.indexOf("call")!=-1) {
            final String[] listName={""};
            final String name=fetchName(msgs);
            Log.d("Name",name);

            Dexter.withContext(this)
                    .withPermissions(
                            Manifest.permission.READ_CONTACTS,
                            Manifest.permission.CALL_PHONE
                    ).withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if(report.areAllPermissionsGranted()) {
                                if(checkForPreviousCallList(MainActivity.this)) {
                                    speak(makeCallFromSavedContactList(MainActivity.this, name));
                                } else {
                                    HashMap<String,String> list=getContactList(MainActivity.this,name);
                                    if(list.size()>1) {
                                        for(String i:list.keySet()) {
                                            listName[0]=listName[0].concat("............................!"+i);
                                        }
                                        speak("Which one sir ?..There is "+listName[0]);
                                    } else if(list.size()==1) {
                                        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N) {
                                            makeCall(MainActivity.this,list.values().stream().findFirst().get());
                                            clearContactListSavedData(MainActivity.this);
                                        }
                                    } else {
                                        speak("No contact found !");
                                        clearContactListSavedData(MainActivity.this);
                                    }
                                }
                            }

                            if(report.isAnyPermissionPermanentlyDenied()) {}
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                            permissionToken.continuePermissionRequest();
                        }
                    }).check();
        }
    }

    public void startRecording(View view) {
        Intent intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);

        recognizer.startListening(intent);
    }
}
