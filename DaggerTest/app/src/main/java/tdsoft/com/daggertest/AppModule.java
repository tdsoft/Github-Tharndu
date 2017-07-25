package tdsoft.com.daggertest;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Admin on 7/18/2017.
 */
@Module
public class AppModule {



    @Provides
    @Singleton
    Context provideContext(){
        return App.getInstance();
    }


    @Provides
    @Singleton
    Foo provideFoo(){
        return new Foo();
    }


}
