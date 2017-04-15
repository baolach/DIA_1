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

/**
 * Created by Baolach on 14/04/2017.
 */

public class Register extends AppCompatActivity {

    Button goback, register;
    EditText username, password, confirmpassword;//, email, first_name,surname,phone,street,town,county;
    String user_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        confirmpassword = (EditText) findViewById(R.id.password2);


        goback = (Button) findViewById(R.id.backBtn);
        goback.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent goBack = new Intent(Register.this, Login.class);
                startActivity(goBack);
                finish();

            }
        });


        register = (Button) findViewById(R.id.registerBtn);
        register.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                new Thread(new Runnable() {

                    public void run() {
                        insert();
                    }

                }).start();

            }

            protected void insert() {

                try {
                    String user = username.getText().toString().toLowerCase().replace(" ", "");
                    String pass = password.getText().toString();
                    String pass2 = confirmpassword.getText().toString();

                    PreparedStatement stmt = null;
                    PreparedStatement stmt2 = null;
                    Class.forName("org.postgresql.Driver");
                    String url = "jdbc:postgresql://138.68.141.18:5432/fypdia2"; // uses driver to interact with database
                    Connection conn = DriverManager.getConnection(url, "root", "Cassie2007"); // connects to database

                    // selects usernae from db to make sure unique
                    String sql = "select username from getdata_getuser where username=?";
                    stmt = conn.prepareStatement(sql);
                    stmt.setString(1, user);
                    ResultSet rset = stmt.executeQuery();

                    // goes through db with whole loop
                    while (rset.next()) {
                        user_name = rset.getString("username");
                    }

                    if((user.equals("")) || (pass.equals(""))) {
                        runOnUiThread(new Runnable() {
                            public void run() {

                                Toast.makeText(getBaseContext(), "Username or Password cannot be empty", Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                        // if username exists
                        if (user != null && user.equals(user_name)) {
                            runOnUiThread(new Runnable() {
                                public void run() {

                                    Toast.makeText(getBaseContext(), "Username " + user_name + " already exists! Try a different one", Toast.LENGTH_LONG).show();
                                }
                            });

                        } else {
                            if (!pass.equals(pass2) || pass.equals(" ")) {// if a password is entered && password doesnt match 2nd && not empty
                                runOnUiThread(new Runnable() {
                                    public void run() {

                                        Toast.makeText(getBaseContext(), "Passwords don't match! Try again!", Toast.LENGTH_LONG).show();
                                    }
                                });

                            } else {
                                // insert new username and password into db
                                String insertdb = "insert into getdata_getuser values (?,?)";
                                stmt2 = conn.prepareStatement(insertdb);
                                stmt2.setString(1, user);
                                stmt2.setString(2, pass);

                                stmt2.execute();
                                stmt2.close();
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText(getBaseContext(), "Account created! Login with your new account details ", Toast.LENGTH_LONG).show();
                                        Intent login = new Intent(Register.this, Login.class);
                                        startActivity(login);
                                        finish();
                                    }
                                });
                            }
                        }
                    //} // end if null

                    stmt.close();
                    conn.close();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }


            }

        });

    } // end onCreate
}
