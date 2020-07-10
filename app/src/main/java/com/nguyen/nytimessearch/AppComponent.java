package com.nguyen.nytimessearch;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {NetworkModule.class})
interface AppComponent {
    void inject(MainActivity activity);
}
