package ru.snatcher.stoket.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import ru.snatcher.stoket.ui.activity.intro.IntroActivity;
import ru.snatcher.stoket.ui.activity.main.MainActivity;

@Module
public interface ActivityModule {
    @ContributesAndroidInjector(modules = FragmentBuildersModule.class)
    MainActivity contributeMainActivity();

    @ContributesAndroidInjector(modules = FragmentBuildersModule.class)
    IntroActivity contributeIntroActivity();
}