package com.project.saransh.rule95;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public ImageView imageView1, imageView2, imageView3;

    TextView scoreView;
    Button playButton;
    Button resetButton;
    int posA=1;
    int posB=2;
    int posC=3;
    Random random;

    long timeEachMove;
    long timeBetweenMoves;
    int numberOfMoves;
    int score=0;
    int flag=1;

    int posLeft, posCenter, posRight;

    ImageView arrow1, arrow2, arrow3;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    int highScore;
    TextView highScoreView;

    TextView instructionsView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        imageView1=(ImageView)findViewById(R.id.imageView3);
        imageView2=(ImageView)findViewById(R.id.imageView4);
        imageView3=(ImageView)findViewById(R.id.imageView5);


        imageView1.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int x=imageView1.getLeft();
                int y=imageView1.getTop();
                posLeft=x;
                if(Build.VERSION.SDK_INT < 16) {
                    imageView1.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }else {
                    imageView1.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });

        imageView2.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int x=imageView2.getLeft();
                int y=imageView2.getTop();
                posCenter=x;
                if(Build.VERSION.SDK_INT < 16) {
                    imageView1.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }else {
                    imageView1.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });

        imageView3.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int x=imageView3.getLeft();
                int y=imageView3.getTop();
                posRight=x;
                if(Build.VERSION.SDK_INT < 16) {
                    imageView1.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }else {
                    imageView1.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });

        scoreView=(TextView)findViewById(R.id.scoreView);
        playButton=(Button)findViewById(R.id.button);
        resetButton=(Button)findViewById(R.id.button2);

        resetButton.setVisibility(View.GONE);

        arrow1=(ImageView)findViewById(R.id.imageView6);
        arrow2=(ImageView)findViewById(R.id.imageView7);
        arrow3=(ImageView)findViewById(R.id.imageView8);

        arrow1.setVisibility(View.GONE);
        arrow2.setVisibility(View.GONE);
        arrow3.setVisibility(View.GONE);

        final Animation animation= new AlphaAnimation(1, 0);
        animation.setDuration(500);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.REVERSE);

        if(posA==1){
            arrow1.setVisibility(View.VISIBLE);
            arrow1.startAnimation(animation);
        }else if(posA==2){
            arrow2.setVisibility(View.VISIBLE);
            arrow2.startAnimation(animation);
        }else if(posA==3){
            arrow3.setVisibility(View.VISIBLE);
            arrow3.startAnimation(animation);
        }


        sharedPreferences=this.getSharedPreferences("HighScore", Context.MODE_PRIVATE);
        highScore=sharedPreferences.getInt("score", 0);
        editor=sharedPreferences.edit();
        highScoreView=(TextView)findViewById(R.id.highScoreView);
        highScoreView.setText("High Score: "+highScore);

        LinearLayout viewGroup=(LinearLayout)findViewById(R.id.popup);
        LayoutInflater layoutInflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout=layoutInflater.inflate(R.layout.instructions_popup, viewGroup);

        try {
            final PopupWindow popupWindow = new PopupWindow();
            popupWindow.setContentView(layout);
            popupWindow.setWidth(ConstraintLayout.LayoutParams.WRAP_CONTENT);
            popupWindow.setHeight(ConstraintLayout.LayoutParams.WRAP_CONTENT);
            popupWindow.setFocusable(true);

            findViewById(R.id.main).post(new Runnable() {
                @Override
                public void run() {
                    popupWindow.showAtLocation(findViewById(R.id.main), Gravity.CENTER, 0, 0);
                    Button closePopup = (Button) layout.findViewById(R.id.button3);
                    closePopup.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popupWindow.dismiss();
                        }
                    });
                }
            });
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Error in Popup", Toast.LENGTH_SHORT).show();
        }

        instructionsView=(TextView)findViewById(R.id.textView3);
        instructionsView.setText("Click Play to Start Game");
    }

    public void animate(View view) {
        if(flag==0){
            return;
        }
        flag=0;



        imageView1 = (ImageView) findViewById(R.id.imageView3);
        imageView2 = (ImageView) findViewById(R.id.imageView4);
        imageView3 = (ImageView) findViewById(R.id.imageView5);
        random = new Random();

        imageView1.setOnClickListener(null);
        imageView2.setOnClickListener(null);
        imageView3.setOnClickListener(null);

        arrow1.clearAnimation();
        arrow2.clearAnimation();
        arrow3.clearAnimation();

        arrow1.setVisibility(View.GONE);
        arrow2.setVisibility(View.GONE);
        arrow3.setVisibility(View.GONE);

        scoreView.setText("Score: "+score);


        final int distAC=(posRight-posLeft)/8;
        final int distAB=(posCenter-posLeft)/8;
        final int distBC=(posRight-posCenter)/8;

        if(score==0){
            timeEachMove=400;
            timeBetweenMoves=600;
            numberOfMoves=10;
        }

        if(score>0 && score<5){
            timeBetweenMoves-=50;
        }else if(score>=5 && score<10){
            timeEachMove-=50;
            numberOfMoves+=1;
        }else if(score>=10 && score<12){
            timeBetweenMoves-=50;
        }else if(score==15 || score==20 || score==25) {
            timeBetweenMoves -= 10;
            numberOfMoves += 1;
        }else if(score==30 || score==35 || score==40){
            timeEachMove -= 10;
        }


        Handler handler=new Handler();

        for(int i=0; i<numberOfMoves; i++) {

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    int a=0, b=0;

                    a = random.nextInt(3) + 1;
                    do {
                        b = random.nextInt(3) + 1;
                    } while (a == b);

                    if ((a == 1 && b == 3) || (a == 3 && b == 1)) {
                        if (posA == 1) {

                            ObjectAnimator animation1 = ObjectAnimator.ofFloat(imageView1, "translationX", distAC, (2*distAC), (3*distAC),(4*distAC),(5*distAC), (6*distAC), (7*distAC), (8*distAC));
                            ObjectAnimator animation2 = ObjectAnimator.ofFloat(imageView1, "translationY", 60f, 120f, 180f, 240f, 180f, 120f, 60f, 0f);
                            animation1.setDuration(timeEachMove);
                            animation2.setDuration(timeEachMove);
                            animation1.start();
                            animation2.start();
                            posA = 3;
                        } else if (posA == 3) {
                            ObjectAnimator animation1 = ObjectAnimator.ofFloat(imageView1, "translationX", (7*distAC), (6*distAC), (5*distAC), (4*distAC), (3*distAC), (2*distAC), (1*distAC), 0f);
                            ObjectAnimator animation2 = ObjectAnimator.ofFloat(imageView1, "translationY", -60f, -120f, -180f, -240f, -180f, -120f, -60f, 0f);
                            animation1.setDuration(timeEachMove);
                            animation2.setDuration(timeEachMove);
                            animation1.start();
                            animation2.start();
                            posA = 1;
                        }
                        if (posC == 3) {
                            ObjectAnimator animation3 = ObjectAnimator.ofFloat(imageView3, "translationX", -distAC, -(2*distAC), -(3*distAC),-(4*distAC), -(5*distAC), -(6*distAC), -(7*distAC), -(8*distAC));
                            ObjectAnimator animation4 = ObjectAnimator.ofFloat(imageView3, "translationY", -60f, -120f, -180f, -240f, -180f, -120f, -60f, 0f);
                            animation3.setDuration(timeEachMove);
                            animation4.setDuration(timeEachMove);
                            animation3.start();
                            animation4.start();
                            posC = 1;
                        } else if (posC == 1) {

                            ObjectAnimator animation3 = ObjectAnimator.ofFloat(imageView3, "translationX", -(7*distAC), -(6*distAC), -(5*distAC), -(4*distAC), -(3*distAC), -(2*distAC), -(1*distAC), 0f);
                            ObjectAnimator animation4 = ObjectAnimator.ofFloat(imageView3, "translationY", 60f, 120f, 180f, 240f, 180f, 120f, 60f, 0f);
                            animation3.setDuration(timeEachMove);
                            animation4.setDuration(timeEachMove);
                            animation3.start();
                            animation4.start();
                            posC = 3;
                        }
                        if (posB == 3) {
                            ObjectAnimator animation3 = ObjectAnimator.ofFloat(imageView2, "translationX", (3*distAC), (2*distAC), (distAC), 0f, -(distAC), -(2*distAC), -(3*distAC), -(posCenter-posLeft));
                            ObjectAnimator animation4 = ObjectAnimator.ofFloat(imageView2, "translationY", -60f, -120f, -180f, -240f, -180f, -120f, -60f, 0f);
                            animation3.setDuration(timeEachMove);
                            animation4.setDuration(timeEachMove);
                            animation3.start();
                            animation4.start();
                            posB = 1;
                        } else if (posB == 1) {

                            ObjectAnimator animation3 = ObjectAnimator.ofFloat(imageView2, "translationX", -(3*distAC), -(2*distAC), -(distAC), 0f, (distAC), (2*distAC), (3*distAC), (posRight-posCenter));
                            ObjectAnimator animation4 = ObjectAnimator.ofFloat(imageView2, "translationY", 60f, 120f, 180f, 240f, 180f, 120f, 60f, 0f);
                            animation3.setDuration(timeEachMove);
                            animation4.setDuration(timeEachMove);
                            animation3.start();
                            animation4.start();
                            posB = 3;
                        }



                    } else if ((a == 1 && b == 2) || (a == 2 && b == 1)) {

                        if (posA == 1) {
                            ObjectAnimator animation1 = ObjectAnimator.ofFloat(imageView1, "translationX", distAB, (2*distAB), (3*distAB), (4*distAB), (5*distAB), (6*distAB), (7*distAB), (8*distAB));
                            ObjectAnimator animation2 = ObjectAnimator.ofFloat(imageView1, "translationY", 50f, 100f, 150f, 200f, 150f, 100f, 50f, 0f);
                            animation1.setDuration(timeEachMove);
                            animation2.setDuration(timeEachMove);
                            animation1.start();
                            animation2.start();
                            posA = 2;
                        } else if (posA == 2) {
                            ObjectAnimator animation1 = ObjectAnimator.ofFloat(imageView1, "translationX", (7*distAB), (6*distAB), (5*distAB), (4*distAB), (3*distAB), (2*distAB), (distAB), 0f);
                            ObjectAnimator animation2 = ObjectAnimator.ofFloat(imageView1, "translationY", -50f, -100f, -150f, -200f, -150f, -100f, -50f, 0f);
                            animation1.setDuration(timeEachMove);
                            animation2.setDuration(timeEachMove);
                            animation1.start();
                            animation2.start();
                            posA = 1;
                        }
                        if (posC == 1) {
                            ObjectAnimator animation1 = ObjectAnimator.ofFloat(imageView3, "translationX", -(7*distAC), -(13*distAC/2), -(6*distAC), -(11*distAC/2), -(5*distAC), -(9*distAC/2), -(4*distAC), -(posRight-posCenter));
                            ObjectAnimator animation2 = ObjectAnimator.ofFloat(imageView3, "translationY", 50f, 100f, 150f, 200f, 150f, 100f, 50f, 0f);
                            animation1.setDuration(timeEachMove);
                            animation2.setDuration(timeEachMove);
                            animation1.start();
                            animation2.start();
                            posC = 2;
                        } else if (posC == 2) {
                            ObjectAnimator animation1 = ObjectAnimator.ofFloat(imageView3, "translationX", -(9*distAC/2), -(5*distAC), -(11*distAC/2), -(6*distAC), -(13*distAC/2), -(7*distAC), -(15*distAC/2), -(8*distAC));
                            ObjectAnimator animation2 = ObjectAnimator.ofFloat(imageView3, "translationY", -50f, -100f, -150f, -200f, -150f, -100f, -50f, 0f);
                            animation1.setDuration(timeEachMove);
                            animation2.setDuration(timeEachMove);
                            animation1.start();
                            animation2.start();
                            posC = 1;
                        }
                        if (posB == 1) {
                            ObjectAnimator animation1 = ObjectAnimator.ofFloat(imageView2, "translationX", -(7*distAB), -(6*distAB), -(5*distAB), -(4*distAB), -(3*distAB), -(2*distAB), -(distAB), 0);
                            ObjectAnimator animation2 = ObjectAnimator.ofFloat(imageView2, "translationY", 50f, 100f, 150f, 200f, 150f, 100f, 50f, 0f);
                            animation1.setDuration(timeEachMove);
                            animation2.setDuration(timeEachMove);
                            animation1.start();
                            animation2.start();
                            posB = 2;
                        } else if (posB == 2) {
                            ObjectAnimator animation1 = ObjectAnimator.ofFloat(imageView2, "translationX", -distAB, -(2*distAB), -(3*distAB), -(4*distAB), -(5*distAB), -(6*distAB), -(7*distAB), -(8*distAB));
                            ObjectAnimator animation2 = ObjectAnimator.ofFloat(imageView2, "translationY", -50f, -100f, -150f, -200f, -150f, -100f, -50f, 0f);
                            animation1.setDuration(timeEachMove);
                            animation2.setDuration(timeEachMove);
                            animation1.start();
                            animation2.start();
                            posB = 1;
                        }
                    } else if ((a == 2 && b == 3) || (a == 3 && b == 2)) {

                        if (posA == 2) {
                            ObjectAnimator animation1 = ObjectAnimator.ofFloat(imageView1, "translationX", (9*distAC/2), (5*distAC), (11*distAC/2), (6*distAC), (13*distAC/2), (7*distAC), (15*distAC/2), (8*distAC));
                            ObjectAnimator animation2 = ObjectAnimator.ofFloat(imageView1, "translationY", 50f, 100f, 150f, 200f, 150f, 100f, 50f, 0f);
                            animation1.setDuration(timeEachMove);
                            animation2.setDuration(timeEachMove);
                            animation1.start();
                            animation2.start();
                            posA = 3;
                        } else if (posA == 3) {
                            ObjectAnimator animation1 = ObjectAnimator.ofFloat(imageView1, "translationX", (15*distAC/2), (7*distAC), (13*distAC/2), (6*distAC), (11*distAC/2), (5*distAC), (9*distAC/2), (posRight-posCenter));
                            ObjectAnimator animation2 = ObjectAnimator.ofFloat(imageView1, "translationY", -50f, -100f, -150f, -200f, -150f, -100f, -50f, 0f);
                            animation1.setDuration(timeEachMove);
                            animation2.setDuration(timeEachMove);
                            animation1.start();
                            animation2.start();
                            posA = 2;
                        }
                        if (posC == 2) {
                            ObjectAnimator animation1 = ObjectAnimator.ofFloat(imageView3, "translationX", -(7*distBC), -(6*distBC), -(5*distBC), -(4*distBC), -(3*distBC), -(2*distBC), -(distBC), 0);
                            ObjectAnimator animation2 = ObjectAnimator.ofFloat(imageView3, "translationY", 50f, 100f, 150f, 200f, 150f, 100f, 50f, 0f);
                            animation1.setDuration(timeEachMove);
                            animation2.setDuration(timeEachMove);
                            animation1.start();
                            animation2.start();
                            posC = 3;
                        } else if (posC == 3) {
                            ObjectAnimator animation1 = ObjectAnimator.ofFloat(imageView3, "translationX", -distBC, -(2*distBC), -(3*distBC), -(4*distBC), -(5*distBC), -(6*distBC), -(7*distBC), -(8*distBC));
                            ObjectAnimator animation2 = ObjectAnimator.ofFloat(imageView3, "translationY", -50f, -100f, -150f, -200f, -150f, -100f, -50f, 0f);
                            animation1.setDuration(timeEachMove);
                            animation2.setDuration(timeEachMove);
                            animation1.start();
                            animation2.start();
                            posC = 2;
                        }
                        if (posB == 2) {
                            ObjectAnimator animation1 = ObjectAnimator.ofFloat(imageView2, "translationX", distBC, (2*distBC), (3*distBC), (4*distBC), (5*distBC), (6*distBC), (7*distBC), (8*distBC));
                            ObjectAnimator animation2 = ObjectAnimator.ofFloat(imageView2, "translationY", 50f, 100f, 150f, 200f, 150f, 100f, 50f, 0f);
                            animation1.setDuration(timeEachMove);
                            animation2.setDuration(timeEachMove);
                            animation1.start();
                            animation2.start();
                            posB = 3;
                        } else if (posB == 3) {
                            ObjectAnimator animation1 = ObjectAnimator.ofFloat(imageView2, "translationX", (7*distBC), (6*distBC), (5*distBC), (4*distBC), (3*distBC), (2*distBC), (distBC), 0);
                            ObjectAnimator animation2 = ObjectAnimator.ofFloat(imageView2, "translationY", -50f, -100f, -150f, -200f, -150f, -100f, -50f, 0f);
                            animation1.setDuration(timeEachMove);
                            animation2.setDuration(timeEachMove);
                            animation1.start();
                            animation2.start();
                            posB = 2;
                        }
                    }

                    instructionsView.setText("Shuffling now...");

                }
            },timeBetweenMoves * i);
        }

        Handler handler1=new Handler();

        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                instructionsView.setText("Select the correct box now");
                try {
                    imageView1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            score++;
                            Toast.makeText(getApplicationContext(), "Correct! Score updated to: "+score, Toast.LENGTH_SHORT).show();
                            if(highScore<score){
                                Toast.makeText(getApplicationContext(), "New High Score: "+score, Toast.LENGTH_SHORT).show();
                                editor.putInt("score", score);
                                editor.commit();
                                highScore=sharedPreferences.getInt("score", 0);
                                highScoreView.setText("High Score: "+highScore);
                            }
                            scoreView.setText("Score: "+score);
                            imageView1.setOnClickListener(null);
                            imageView2.setOnClickListener(null);
                            imageView3.setOnClickListener(null);
                            final Animation animation= new AlphaAnimation(1, 0);
                            animation.setDuration(500);
                            animation.setInterpolator(new LinearInterpolator());
                            animation.setRepeatCount(Animation.INFINITE);
                            animation.setRepeatMode(Animation.REVERSE);
                            instructionsView.setText("Click Play for next Level");

                            if(posA==1){
                                arrow1.setVisibility(View.VISIBLE);
                                arrow1.startAnimation(animation);
                            }else if(posA==2){
                                arrow2.setVisibility(View.VISIBLE);
                                arrow2.startAnimation(animation);
                            }else if(posA==3){
                                arrow3.setVisibility(View.VISIBLE);
                                arrow3.startAnimation(animation);
                            }

                            flag=1;
                            return;
                        }
                    });
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Error in OnClick listener", Toast.LENGTH_SHORT).show();
                }

                try {
                    imageView2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getApplicationContext(), "Selected the wrong box. Game Over.", Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(), "YOUR FINAL SCORE: "+score, Toast.LENGTH_SHORT).show();
                            score=0;
                            imageView1.setOnClickListener(null);
                            imageView2.setOnClickListener(null);
                            imageView3.setOnClickListener(null);

                            instructionsView.setText("Click Reset to Start Game again");

                            final Animation animation= new AlphaAnimation(1, 0);
                            animation.setDuration(500);
                            animation.setInterpolator(new LinearInterpolator());
                            animation.setRepeatCount(Animation.INFINITE);
                            animation.setRepeatMode(Animation.REVERSE);

                            if(posA==1){
                                arrow1.setVisibility(View.VISIBLE);
                                arrow1.startAnimation(animation);
                            }else if(posA==2){
                                arrow2.setVisibility(View.VISIBLE);
                                arrow2.startAnimation(animation);
                            }else if(posA==3){
                                arrow3.setVisibility(View.VISIBLE);
                                arrow3.startAnimation(animation);
                            }


                            resetButton.setVisibility(View.VISIBLE);
                            playButton.setVisibility(View.GONE);

                            flag=1;
                            return;
                        }
                    });
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Error in OnClick listener", Toast.LENGTH_SHORT).show();
                }

                try {
                    imageView3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getApplicationContext(), "Selected the wrong box. Game Over.", Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(), "YOUR FINAL SCORE: "+score, Toast.LENGTH_SHORT).show();
                            score=0;
                            imageView1.setOnClickListener(null);
                            imageView2.setOnClickListener(null);
                            imageView3.setOnClickListener(null);

                            final Animation animation= new AlphaAnimation(1, 0);
                            animation.setDuration(500);
                            animation.setInterpolator(new LinearInterpolator());
                            animation.setRepeatCount(Animation.INFINITE);
                            animation.setRepeatMode(Animation.REVERSE);

                            if(posA==1){
                                arrow1.setVisibility(View.VISIBLE);
                                arrow1.startAnimation(animation);
                            }else if(posA==2){
                                arrow2.setVisibility(View.VISIBLE);
                                arrow2.startAnimation(animation);
                            }else if(posA==3){
                                arrow3.setVisibility(View.VISIBLE);
                                arrow3.startAnimation(animation);
                            }


                            resetButton.setVisibility(View.VISIBLE);
                            playButton.setVisibility(View.GONE);

                            flag=1;
                            return;
                        }
                    });
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Error in OnClick listener", Toast.LENGTH_SHORT).show();
                }
            }
        }, timeBetweenMoves*numberOfMoves);

    }


    public void reset(View view){
        playButton.setVisibility(View.VISIBLE);
        scoreView.setText("Score: "+0);
        resetButton.setVisibility(View.GONE);
        instructionsView.setText("Click Play to Start Game");
    }
}
