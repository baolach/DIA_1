package com.example.baolach.driving_app_3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login extends AppCompatActivity {

    Button login, register;
    EditText username , password;
    static String passw,usern;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        login = (Button) findViewById(R.id.loginBtn);
        register = (Button) findViewById(R.id.registerBtn);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable(){

                    public void run()
                    {
                        authenticate();
                    }

                }).start();
            }

            void authenticate() {

                try
                {
                    final String user = username.getText().toString().replace(" ", ""); // replace gets rid of spaces
                    String pass= password.getText().toString().replace(" ", "");

                    PreparedStatement st = null;
                    Class.forName("org.postgresql.Driver");
                    String url = "jdbc:postgresql://138.68.141.18:5432/fypdia2"; // uses driver to interact with database
                    Connection conn = DriverManager.getConnection(url, "root", "Cassie2007"); // connects to database

                    String sql = "select password , username from getdata_getuser where username=?";
                    st = conn.prepareStatement(sql);
                    st.setString(1, user);
                    ResultSet rs = st.executeQuery();


                    while(rs.next())
                    {
                        passw = rs.getString("password");
                        usern = rs.getString("username"); // changes to lowercase to compare in the db
                    }

                    if(usern != null && user.equals(usern))
                    {
                        if(passw  != null && pass.equals(passw))
                        {
                            runOnUiThread(new Runnable() {
                                public void run() {

                                    Toast.makeText(getBaseContext(), " Welcome "+ user + "!", Toast.LENGTH_SHORT).show();
                                }
                            });
                            Intent mainActivity = new Intent(Login.this, MainActivity.class);
                            startActivity(mainActivity);
                            finish();
                        }
                        else
                        {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(getBaseContext(), "Incorrect password" , Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                    else
                    {
                        runOnUiThread(new Runnable() {
                            public void run() {

                                Toast.makeText(getBaseContext(), "User does not exist. Please Register", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    st.close();
                    conn.close();
                }
                catch (ClassNotFoundException e)
                {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent k = new Intent(Login.this, Register.class);
                Toast.makeText(Login.this, "Create new username and password for your Driving Instructor Account", Toast.LENGTH_LONG).show();
                startActivity(k);
                finish();
            }
        });
    }
}
