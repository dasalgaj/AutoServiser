package com.example.testapp.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.testapp.fragments.LoginFragment;
import com.example.testapp.fragments.RegisterFragment;
import com.example.testapp.listeners.LoginListener;

public class ViewPagerAdapter extends FragmentStateAdapter implements LoginListener {

    String email;
    String password;

    public ViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        Fragment fragment;

        switch (position)
        {
            case 1:
                fragment = new RegisterFragment();
                ((RegisterFragment)fragment).loginListener = this;
                break;
            default:
                fragment = new LoginFragment();
                ((LoginFragment)fragment).loginListener = this;
                break;
        }

        return fragment;
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getLozinka() {
        return password;
    }
}
