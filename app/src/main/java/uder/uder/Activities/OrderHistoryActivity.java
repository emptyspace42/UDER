package uder.uder.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import uder.uder.HelperClasses.Order;
import uder.uder.HelperClasses.Product;
import uder.uder.HelperClasses.Regular_User;
import uder.uder.HelperClasses.RequestClass;
import uder.uder.HelperClasses.ShoppingCart;
import uder.uder.OrderAdapter;
import uder.uder.R;

public class OrderHistoryActivity extends AppCompatActivity {

    private Regular_User currentUser;
    private ArrayList<Order> orders;
    private OrderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        currentUser = (Regular_User) getIntent().getSerializableExtra("user");
        final TextView orderHistoryMessage = (TextView) findViewById(R.id.tvOrderHistoryMessage);
        orderHistoryMessage.setText("Displaying Order History for " + currentUser.getfName() + " " + currentUser.getlName());

        getOrderHistory();

        ListView orderHistoryList = (ListView) findViewById(R.id.lv_orderHistoryList);
        final ImageButton goBack = (ImageButton) findViewById(R.id.b_goBackOrderHistory);
        adapter = new OrderAdapter(getApplicationContext(), orders);
        orderHistoryList.setAdapter(adapter);



        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backToUserPage = new Intent(v.getContext(), UserActivity.class);
                backToUserPage.putExtra("user", currentUser);
                v.getContext().startActivity(backToUserPage);
            }
        });

    }


    public void getOrderHistory(){

        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response.toString());
                try {
                    if (response.getString("status").equals("OK")) {
                        JSONArray orderArray = response.getJSONArray("orders");
                        for (int i = 0; i < orderArray.length(); i++) {
                            JSONObject order = orderArray.getJSONObject(i);
                            JSONArray products = order.getJSONArray("products");
                            ShoppingCart productQuantities = new ShoppingCart();

                            String street = order.getString("street");
                            String city = order.getString("city");
                            String state = order.getString("state");
                            String zip = order.getString("zip");
                            String created = order.getString("date");
                            String status = order.getString("status");
                            String order_id = order.getString("order_id");
                            String milker_id = order.getString("milker_id");
                            String buyer_id = order.getString("buyer_id");
                            HashMap<String, String> address = new HashMap<>();
                            address.put("street", street);
                            address.put("city", city);
                            address.put("state", state);
                            address.put("zip", zip);
                            for (int j = 0; j < products.length(); j++) {
                                JSONObject aProduct = products.getJSONObject(j);
                                String product_id = aProduct.getString("product_id");
                                String name = aProduct.getString("name");
                                String price = aProduct.getString("price");
                                Product p = new Product(product_id, name, price);
                                String quantity = aProduct.getString("quantity");
                                productQuantities.put(p, quantity);
                            }
                            Order o = new Order(order_id, address, buyer_id, status, milker_id, created, productQuantities);
                            orders.add(o);
                        }
                    }

                }catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter.notifyDataSetChanged();
            }
        };

        RequestClass getOrderHistoryFromServer = new RequestClass(String.format("http://34.208.156.179:4567/api/v1/orders/%s/history", currentUser.getUserID()), null, listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
                AlertDialog.Builder serverError = new AlertDialog.Builder(OrderHistoryActivity.this);
                serverError.setTitle("Something went wrong :(");
                serverError.setMessage("Sorry your request could not be completed at this time. Try again later.");
                serverError.setNegativeButton("Back to Main Page", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent userActivity = new Intent(getApplicationContext(), UserActivity.class);
                        userActivity.putExtra("user", currentUser);
                        userActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getApplicationContext().startActivity(userActivity);
                    }
                }).create().show();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(OrderHistoryActivity.this);
        orders = new ArrayList<>();
        queue.add(getOrderHistoryFromServer);



    }
}