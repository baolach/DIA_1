package com.example.baolach.driving_app_3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
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

public class Register extends AppCompatActivity{

    Button back,register;
    EditText username , password, password2;//, email, first_name,surname,phone,street,town,county;
    String u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        password2 = (EditText) findViewById(R.id.password2);


        back.setOnClickListener(new View.OnClickListener(){

            public void onClick (View v)
            {
                Intent back = new Intent(Register.this, Login.class);
                startActivity(back);
                finish();
            }
        });

        register.setOnClickListener(new View.OnClickListener(){

            public void onClick (View v)
            {
                new Thread(new Runnable(){

                    public void run()
                    {
                        insert();
                    }

                }).start();

            }
            protected void insert() {

                try
                {
                    String user = username.getText().toString().toLowerCase();
                    String pass= password.getText().toString();
                    String pass2 = password2.getText().toString();

                    PreparedStatement st = null;
                    PreparedStatement st2 = null;
                    Class.forName("org.postgresql.Driver");
                    String url = "jdbc:postgresql://138.68.141.18:5432/fypdia2"; // uses driver to interact with database
                    Connection conn = DriverManager.getConnection(url, "root", "Cassie2007"); // connects to database
                    String sql = "select username from getdata_getuser where username=?";
                    st = conn.prepareStatement(sql);
                    st.setString(1, user);
                    ResultSet rs = st.executeQuery();


                    while(rs.next())
                    {
                        u = rs.getString("username");
                    }
                    if(u != null && user.equals(u))
                    {
                        Toast.makeText(getBaseContext(), "User " + u + " already exist! Try different username!" , Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        if(pass != null && !pass.equals(pass2) && pass == " ")
                        {
                            runOnUiThread(new Runnable() {
                                public void run() {

                                    Toast.makeText(getBaseContext(), "Passwords don't match! Try again!", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                        else
                        {
                            String inss = "insert into getdata_getuser values (?,?,?,?,?,?,?,?,?)";
                            st2 = conn.prepareStatement(inss);
                            st2.setString(1,user);
                            st2.setString(2,pass);

                            st2.execute();
                            st2.close();
                            runOnUiThread(new Runnable() {
                                public void run() {

                                    Toast.makeText(getBaseContext(), "User Created! ", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
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
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent k = new Intent(Register.this, Login.class);
            startActivity(k);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
