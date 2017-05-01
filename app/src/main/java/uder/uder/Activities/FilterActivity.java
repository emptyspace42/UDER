package uder.uder.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

import uder.uder.HelperClasses.Filter;
import uder.uder.HelperClasses.Regular_User;
import uder.uder.HelperClasses.ShoppingCart;
import uder.uder.HelperClasses.User;
import uder.uder.R;

/**
 * Created by cazza223 on 3/20/2017.
 */


public class FilterActivity extends AppCompatActivity {
    private Regular_User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        // https://developer.android.com/guide/topics/ui/controls/spinner.html

        final Spinner milk_type_spinner = (Spinner)findViewById(R.id.sp_typeList);
        final Spinner milk_price_spinner = (Spinner)findViewById(R.id.sp_priceRange);
        final Spinner milk_flavor_spinner = (Spinner)findViewById(R.id.sp_flavor);
        final Spinner milk_brand_spinner = (Spinner)findViewById(R.id.sp_brand);
        final ImageButton b_cancel = (ImageButton)findViewById(R.id.b_cancel);
        final ImageButton b_apply = (ImageButton)findViewById(R.id.b_apply);

        currentUser = (Regular_User)getIntent().getSerializableExtra("user");

        ArrayAdapter<CharSequence> typeSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.milk_types, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> priceSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.milk_prices, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> flavorSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.milk_flavors, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> brandSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.milk_brands, android.R.layout.simple_spinner_item);

        typeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        priceSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        flavorSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        brandSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        milk_type_spinner.setAdapter(typeSpinnerAdapter);
        milk_price_spinner.setAdapter(priceSpinnerAdapter);
        milk_flavor_spinner.setAdapter(flavorSpinnerAdapter);
        milk_brand_spinner.setAdapter(brandSpinnerAdapter);

        int typePos = typeSpinnerAdapter.getPosition(currentUser.getFilter().getMilk_type());
        milk_type_spinner.setSelection(typePos);

        int pricePos = priceSpinnerAdapter.getPosition(currentUser.getFilter().getMilk_price_range());
        milk_price_spinner.setSelection(pricePos);

        int flavorPos = flavorSpinnerAdapter.getPosition(currentUser.getFilter().getMilk_flavor());
        milk_flavor_spinner.setSelection(flavorPos);

        int brandPos = brandSpinnerAdapter.getPosition(currentUser.getFilter().getMilk_brand());
        milk_brand_spinner.setSelection(brandPos);

        b_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type = milk_type_spinner.getSelectedItem().toString();
                String priceRange = milk_price_spinner.getSelectedItem().toString();
                String flavor = milk_flavor_spinner.getSelectedItem().toString();
                String brand = milk_brand_spinner.getSelectedItem().toString();

                currentUser.getFilter().setMilk_brand(brand);
                currentUser.getFilter().setMilk_flavor(flavor);
                currentUser.getFilter().setMilk_price_range(priceRange);
                currentUser.getFilter().setMilk_type(type);

                Intent returnIntent = new Intent(v.getContext(), UserActivity.class);

                returnIntent.putExtra("user", currentUser);

                v.getContext().startActivity(returnIntent);
            }
        });

        b_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent(v.getContext(), UserActivity.class);
                returnIntent.putExtra("user", currentUser);
                v.getContext().startActivity(returnIntent);
            }
        });

    }

}
