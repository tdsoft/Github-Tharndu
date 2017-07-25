package tdsoft.com.daggertest;

/**
 * Created by Admin on 7/18/2017.
 */

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface ApplicationComponent {

    void inject(App app);

    void inject(MainActivity mainActivity);
}
