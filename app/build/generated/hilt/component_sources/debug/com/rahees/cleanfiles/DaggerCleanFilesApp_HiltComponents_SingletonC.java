package com.rahees.cleanfiles;

import android.app.Activity;
import android.app.Service;
import android.view.View;
import androidx.datastore.core.DataStore;
import androidx.datastore.preferences.core.Preferences;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import com.rahees.cleanfiles.data.FileRepository;
import com.rahees.cleanfiles.data.PreferencesManager;
import com.rahees.cleanfiles.data.StorageRepository;
import com.rahees.cleanfiles.data.local.AppDatabase;
import com.rahees.cleanfiles.data.local.RecentDao;
import com.rahees.cleanfiles.di.AppModule_ProvideDataStoreFactory;
import com.rahees.cleanfiles.di.AppModule_ProvideDatabaseFactory;
import com.rahees.cleanfiles.di.AppModule_ProvideRecentDaoFactory;
import com.rahees.cleanfiles.ui.analysis.AnalysisViewModel;
import com.rahees.cleanfiles.ui.analysis.AnalysisViewModel_HiltModules;
import com.rahees.cleanfiles.ui.browser.BrowserViewModel;
import com.rahees.cleanfiles.ui.browser.BrowserViewModel_HiltModules;
import com.rahees.cleanfiles.ui.category.CategoryViewModel;
import com.rahees.cleanfiles.ui.category.CategoryViewModel_HiltModules;
import com.rahees.cleanfiles.ui.cleaner.CleanerViewModel;
import com.rahees.cleanfiles.ui.cleaner.CleanerViewModel_HiltModules;
import com.rahees.cleanfiles.ui.duplicates.DuplicatesViewModel;
import com.rahees.cleanfiles.ui.duplicates.DuplicatesViewModel_HiltModules;
import com.rahees.cleanfiles.ui.home.HomeViewModel;
import com.rahees.cleanfiles.ui.home.HomeViewModel_HiltModules;
import com.rahees.cleanfiles.ui.search.SearchViewModel;
import com.rahees.cleanfiles.ui.search.SearchViewModel_HiltModules;
import com.rahees.cleanfiles.ui.settings.SettingsViewModel;
import com.rahees.cleanfiles.ui.settings.SettingsViewModel_HiltModules;
import dagger.hilt.android.ActivityRetainedLifecycle;
import dagger.hilt.android.ViewModelLifecycle;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.android.internal.builders.ServiceComponentBuilder;
import dagger.hilt.android.internal.builders.ViewComponentBuilder;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories_InternalFactoryFactory_Factory;
import dagger.hilt.android.internal.managers.ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory;
import dagger.hilt.android.internal.managers.SavedStateHandleHolder;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.hilt.android.internal.modules.ApplicationContextModule_ProvideContextFactory;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.IdentifierNameString;
import dagger.internal.KeepFieldType;
import dagger.internal.LazyClassKeyMap;
import dagger.internal.MapBuilder;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class DaggerCleanFilesApp_HiltComponents_SingletonC {
  private DaggerCleanFilesApp_HiltComponents_SingletonC() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private ApplicationContextModule applicationContextModule;

    private Builder() {
    }

    public Builder applicationContextModule(ApplicationContextModule applicationContextModule) {
      this.applicationContextModule = Preconditions.checkNotNull(applicationContextModule);
      return this;
    }

    public CleanFilesApp_HiltComponents.SingletonC build() {
      Preconditions.checkBuilderRequirement(applicationContextModule, ApplicationContextModule.class);
      return new SingletonCImpl(applicationContextModule);
    }
  }

  private static final class ActivityRetainedCBuilder implements CleanFilesApp_HiltComponents.ActivityRetainedC.Builder {
    private final SingletonCImpl singletonCImpl;

    private SavedStateHandleHolder savedStateHandleHolder;

    private ActivityRetainedCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ActivityRetainedCBuilder savedStateHandleHolder(
        SavedStateHandleHolder savedStateHandleHolder) {
      this.savedStateHandleHolder = Preconditions.checkNotNull(savedStateHandleHolder);
      return this;
    }

    @Override
    public CleanFilesApp_HiltComponents.ActivityRetainedC build() {
      Preconditions.checkBuilderRequirement(savedStateHandleHolder, SavedStateHandleHolder.class);
      return new ActivityRetainedCImpl(singletonCImpl, savedStateHandleHolder);
    }
  }

  private static final class ActivityCBuilder implements CleanFilesApp_HiltComponents.ActivityC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private Activity activity;

    private ActivityCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ActivityCBuilder activity(Activity activity) {
      this.activity = Preconditions.checkNotNull(activity);
      return this;
    }

    @Override
    public CleanFilesApp_HiltComponents.ActivityC build() {
      Preconditions.checkBuilderRequirement(activity, Activity.class);
      return new ActivityCImpl(singletonCImpl, activityRetainedCImpl, activity);
    }
  }

  private static final class FragmentCBuilder implements CleanFilesApp_HiltComponents.FragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private Fragment fragment;

    private FragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public FragmentCBuilder fragment(Fragment fragment) {
      this.fragment = Preconditions.checkNotNull(fragment);
      return this;
    }

    @Override
    public CleanFilesApp_HiltComponents.FragmentC build() {
      Preconditions.checkBuilderRequirement(fragment, Fragment.class);
      return new FragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragment);
    }
  }

  private static final class ViewWithFragmentCBuilder implements CleanFilesApp_HiltComponents.ViewWithFragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private View view;

    private ViewWithFragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;
    }

    @Override
    public ViewWithFragmentCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public CleanFilesApp_HiltComponents.ViewWithFragmentC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewWithFragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl, view);
    }
  }

  private static final class ViewCBuilder implements CleanFilesApp_HiltComponents.ViewC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private View view;

    private ViewCBuilder(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public ViewCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public CleanFilesApp_HiltComponents.ViewC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, view);
    }
  }

  private static final class ViewModelCBuilder implements CleanFilesApp_HiltComponents.ViewModelC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private SavedStateHandle savedStateHandle;

    private ViewModelLifecycle viewModelLifecycle;

    private ViewModelCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ViewModelCBuilder savedStateHandle(SavedStateHandle handle) {
      this.savedStateHandle = Preconditions.checkNotNull(handle);
      return this;
    }

    @Override
    public ViewModelCBuilder viewModelLifecycle(ViewModelLifecycle viewModelLifecycle) {
      this.viewModelLifecycle = Preconditions.checkNotNull(viewModelLifecycle);
      return this;
    }

    @Override
    public CleanFilesApp_HiltComponents.ViewModelC build() {
      Preconditions.checkBuilderRequirement(savedStateHandle, SavedStateHandle.class);
      Preconditions.checkBuilderRequirement(viewModelLifecycle, ViewModelLifecycle.class);
      return new ViewModelCImpl(singletonCImpl, activityRetainedCImpl, savedStateHandle, viewModelLifecycle);
    }
  }

  private static final class ServiceCBuilder implements CleanFilesApp_HiltComponents.ServiceC.Builder {
    private final SingletonCImpl singletonCImpl;

    private Service service;

    private ServiceCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ServiceCBuilder service(Service service) {
      this.service = Preconditions.checkNotNull(service);
      return this;
    }

    @Override
    public CleanFilesApp_HiltComponents.ServiceC build() {
      Preconditions.checkBuilderRequirement(service, Service.class);
      return new ServiceCImpl(singletonCImpl, service);
    }
  }

  private static final class ViewWithFragmentCImpl extends CleanFilesApp_HiltComponents.ViewWithFragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private final ViewWithFragmentCImpl viewWithFragmentCImpl = this;

    private ViewWithFragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;


    }
  }

  private static final class FragmentCImpl extends CleanFilesApp_HiltComponents.FragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl = this;

    private FragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        Fragment fragmentParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return activityCImpl.getHiltInternalFactoryFactory();
    }

    @Override
    public ViewWithFragmentComponentBuilder viewWithFragmentComponentBuilder() {
      return new ViewWithFragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl);
    }
  }

  private static final class ViewCImpl extends CleanFilesApp_HiltComponents.ViewC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final ViewCImpl viewCImpl = this;

    private ViewCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }
  }

  private static final class ActivityCImpl extends CleanFilesApp_HiltComponents.ActivityC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl = this;

    private ActivityCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, Activity activityParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;


    }

    @Override
    public void injectMainActivity(MainActivity mainActivity) {
    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return DefaultViewModelFactories_InternalFactoryFactory_Factory.newInstance(getViewModelKeys(), new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl));
    }

    @Override
    public Map<Class<?>, Boolean> getViewModelKeys() {
      return LazyClassKeyMap.<Boolean>of(MapBuilder.<String, Boolean>newMapBuilder(8).put(LazyClassKeyProvider.com_rahees_cleanfiles_ui_analysis_AnalysisViewModel, AnalysisViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_rahees_cleanfiles_ui_browser_BrowserViewModel, BrowserViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_rahees_cleanfiles_ui_category_CategoryViewModel, CategoryViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_rahees_cleanfiles_ui_cleaner_CleanerViewModel, CleanerViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_rahees_cleanfiles_ui_duplicates_DuplicatesViewModel, DuplicatesViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_rahees_cleanfiles_ui_home_HomeViewModel, HomeViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_rahees_cleanfiles_ui_search_SearchViewModel, SearchViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_rahees_cleanfiles_ui_settings_SettingsViewModel, SettingsViewModel_HiltModules.KeyModule.provide()).build());
    }

    @Override
    public ViewModelComponentBuilder getViewModelComponentBuilder() {
      return new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public FragmentComponentBuilder fragmentComponentBuilder() {
      return new FragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @Override
    public ViewComponentBuilder viewComponentBuilder() {
      return new ViewCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_rahees_cleanfiles_ui_duplicates_DuplicatesViewModel = "com.rahees.cleanfiles.ui.duplicates.DuplicatesViewModel";

      static String com_rahees_cleanfiles_ui_search_SearchViewModel = "com.rahees.cleanfiles.ui.search.SearchViewModel";

      static String com_rahees_cleanfiles_ui_browser_BrowserViewModel = "com.rahees.cleanfiles.ui.browser.BrowserViewModel";

      static String com_rahees_cleanfiles_ui_cleaner_CleanerViewModel = "com.rahees.cleanfiles.ui.cleaner.CleanerViewModel";

      static String com_rahees_cleanfiles_ui_home_HomeViewModel = "com.rahees.cleanfiles.ui.home.HomeViewModel";

      static String com_rahees_cleanfiles_ui_settings_SettingsViewModel = "com.rahees.cleanfiles.ui.settings.SettingsViewModel";

      static String com_rahees_cleanfiles_ui_category_CategoryViewModel = "com.rahees.cleanfiles.ui.category.CategoryViewModel";

      static String com_rahees_cleanfiles_ui_analysis_AnalysisViewModel = "com.rahees.cleanfiles.ui.analysis.AnalysisViewModel";

      @KeepFieldType
      DuplicatesViewModel com_rahees_cleanfiles_ui_duplicates_DuplicatesViewModel2;

      @KeepFieldType
      SearchViewModel com_rahees_cleanfiles_ui_search_SearchViewModel2;

      @KeepFieldType
      BrowserViewModel com_rahees_cleanfiles_ui_browser_BrowserViewModel2;

      @KeepFieldType
      CleanerViewModel com_rahees_cleanfiles_ui_cleaner_CleanerViewModel2;

      @KeepFieldType
      HomeViewModel com_rahees_cleanfiles_ui_home_HomeViewModel2;

      @KeepFieldType
      SettingsViewModel com_rahees_cleanfiles_ui_settings_SettingsViewModel2;

      @KeepFieldType
      CategoryViewModel com_rahees_cleanfiles_ui_category_CategoryViewModel2;

      @KeepFieldType
      AnalysisViewModel com_rahees_cleanfiles_ui_analysis_AnalysisViewModel2;
    }
  }

  private static final class ViewModelCImpl extends CleanFilesApp_HiltComponents.ViewModelC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ViewModelCImpl viewModelCImpl = this;

    private Provider<AnalysisViewModel> analysisViewModelProvider;

    private Provider<BrowserViewModel> browserViewModelProvider;

    private Provider<CategoryViewModel> categoryViewModelProvider;

    private Provider<CleanerViewModel> cleanerViewModelProvider;

    private Provider<DuplicatesViewModel> duplicatesViewModelProvider;

    private Provider<HomeViewModel> homeViewModelProvider;

    private Provider<SearchViewModel> searchViewModelProvider;

    private Provider<SettingsViewModel> settingsViewModelProvider;

    private ViewModelCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, SavedStateHandle savedStateHandleParam,
        ViewModelLifecycle viewModelLifecycleParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;

      initialize(savedStateHandleParam, viewModelLifecycleParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandle savedStateHandleParam,
        final ViewModelLifecycle viewModelLifecycleParam) {
      this.analysisViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 0);
      this.browserViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 1);
      this.categoryViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 2);
      this.cleanerViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 3);
      this.duplicatesViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 4);
      this.homeViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 5);
      this.searchViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 6);
      this.settingsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 7);
    }

    @Override
    public Map<Class<?>, javax.inject.Provider<ViewModel>> getHiltViewModelMap() {
      return LazyClassKeyMap.<javax.inject.Provider<ViewModel>>of(MapBuilder.<String, javax.inject.Provider<ViewModel>>newMapBuilder(8).put(LazyClassKeyProvider.com_rahees_cleanfiles_ui_analysis_AnalysisViewModel, ((Provider) analysisViewModelProvider)).put(LazyClassKeyProvider.com_rahees_cleanfiles_ui_browser_BrowserViewModel, ((Provider) browserViewModelProvider)).put(LazyClassKeyProvider.com_rahees_cleanfiles_ui_category_CategoryViewModel, ((Provider) categoryViewModelProvider)).put(LazyClassKeyProvider.com_rahees_cleanfiles_ui_cleaner_CleanerViewModel, ((Provider) cleanerViewModelProvider)).put(LazyClassKeyProvider.com_rahees_cleanfiles_ui_duplicates_DuplicatesViewModel, ((Provider) duplicatesViewModelProvider)).put(LazyClassKeyProvider.com_rahees_cleanfiles_ui_home_HomeViewModel, ((Provider) homeViewModelProvider)).put(LazyClassKeyProvider.com_rahees_cleanfiles_ui_search_SearchViewModel, ((Provider) searchViewModelProvider)).put(LazyClassKeyProvider.com_rahees_cleanfiles_ui_settings_SettingsViewModel, ((Provider) settingsViewModelProvider)).build());
    }

    @Override
    public Map<Class<?>, Object> getHiltViewModelAssistedMap() {
      return Collections.<Class<?>, Object>emptyMap();
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_rahees_cleanfiles_ui_analysis_AnalysisViewModel = "com.rahees.cleanfiles.ui.analysis.AnalysisViewModel";

      static String com_rahees_cleanfiles_ui_category_CategoryViewModel = "com.rahees.cleanfiles.ui.category.CategoryViewModel";

      static String com_rahees_cleanfiles_ui_search_SearchViewModel = "com.rahees.cleanfiles.ui.search.SearchViewModel";

      static String com_rahees_cleanfiles_ui_home_HomeViewModel = "com.rahees.cleanfiles.ui.home.HomeViewModel";

      static String com_rahees_cleanfiles_ui_cleaner_CleanerViewModel = "com.rahees.cleanfiles.ui.cleaner.CleanerViewModel";

      static String com_rahees_cleanfiles_ui_settings_SettingsViewModel = "com.rahees.cleanfiles.ui.settings.SettingsViewModel";

      static String com_rahees_cleanfiles_ui_browser_BrowserViewModel = "com.rahees.cleanfiles.ui.browser.BrowserViewModel";

      static String com_rahees_cleanfiles_ui_duplicates_DuplicatesViewModel = "com.rahees.cleanfiles.ui.duplicates.DuplicatesViewModel";

      @KeepFieldType
      AnalysisViewModel com_rahees_cleanfiles_ui_analysis_AnalysisViewModel2;

      @KeepFieldType
      CategoryViewModel com_rahees_cleanfiles_ui_category_CategoryViewModel2;

      @KeepFieldType
      SearchViewModel com_rahees_cleanfiles_ui_search_SearchViewModel2;

      @KeepFieldType
      HomeViewModel com_rahees_cleanfiles_ui_home_HomeViewModel2;

      @KeepFieldType
      CleanerViewModel com_rahees_cleanfiles_ui_cleaner_CleanerViewModel2;

      @KeepFieldType
      SettingsViewModel com_rahees_cleanfiles_ui_settings_SettingsViewModel2;

      @KeepFieldType
      BrowserViewModel com_rahees_cleanfiles_ui_browser_BrowserViewModel2;

      @KeepFieldType
      DuplicatesViewModel com_rahees_cleanfiles_ui_duplicates_DuplicatesViewModel2;
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final ViewModelCImpl viewModelCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          ViewModelCImpl viewModelCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.viewModelCImpl = viewModelCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.rahees.cleanfiles.ui.analysis.AnalysisViewModel 
          return (T) new AnalysisViewModel(singletonCImpl.storageRepositoryProvider.get());

          case 1: // com.rahees.cleanfiles.ui.browser.BrowserViewModel 
          return (T) new BrowserViewModel(singletonCImpl.fileRepositoryProvider.get(), singletonCImpl.preferencesManagerProvider.get());

          case 2: // com.rahees.cleanfiles.ui.category.CategoryViewModel 
          return (T) new CategoryViewModel(singletonCImpl.fileRepositoryProvider.get());

          case 3: // com.rahees.cleanfiles.ui.cleaner.CleanerViewModel 
          return (T) new CleanerViewModel(singletonCImpl.storageRepositoryProvider.get());

          case 4: // com.rahees.cleanfiles.ui.duplicates.DuplicatesViewModel 
          return (T) new DuplicatesViewModel(singletonCImpl.storageRepositoryProvider.get(), singletonCImpl.fileRepositoryProvider.get());

          case 5: // com.rahees.cleanfiles.ui.home.HomeViewModel 
          return (T) new HomeViewModel(singletonCImpl.storageRepositoryProvider.get(), singletonCImpl.fileRepositoryProvider.get(), singletonCImpl.recentDao());

          case 6: // com.rahees.cleanfiles.ui.search.SearchViewModel 
          return (T) new SearchViewModel(singletonCImpl.fileRepositoryProvider.get());

          case 7: // com.rahees.cleanfiles.ui.settings.SettingsViewModel 
          return (T) new SettingsViewModel(singletonCImpl.preferencesManagerProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ActivityRetainedCImpl extends CleanFilesApp_HiltComponents.ActivityRetainedC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl = this;

    private Provider<ActivityRetainedLifecycle> provideActivityRetainedLifecycleProvider;

    private ActivityRetainedCImpl(SingletonCImpl singletonCImpl,
        SavedStateHandleHolder savedStateHandleHolderParam) {
      this.singletonCImpl = singletonCImpl;

      initialize(savedStateHandleHolderParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandleHolder savedStateHandleHolderParam) {
      this.provideActivityRetainedLifecycleProvider = DoubleCheck.provider(new SwitchingProvider<ActivityRetainedLifecycle>(singletonCImpl, activityRetainedCImpl, 0));
    }

    @Override
    public ActivityComponentBuilder activityComponentBuilder() {
      return new ActivityCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public ActivityRetainedLifecycle getActivityRetainedLifecycle() {
      return provideActivityRetainedLifecycleProvider.get();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // dagger.hilt.android.ActivityRetainedLifecycle 
          return (T) ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory.provideActivityRetainedLifecycle();

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ServiceCImpl extends CleanFilesApp_HiltComponents.ServiceC {
    private final SingletonCImpl singletonCImpl;

    private final ServiceCImpl serviceCImpl = this;

    private ServiceCImpl(SingletonCImpl singletonCImpl, Service serviceParam) {
      this.singletonCImpl = singletonCImpl;


    }
  }

  private static final class SingletonCImpl extends CleanFilesApp_HiltComponents.SingletonC {
    private final ApplicationContextModule applicationContextModule;

    private final SingletonCImpl singletonCImpl = this;

    private Provider<StorageRepository> storageRepositoryProvider;

    private Provider<AppDatabase> provideDatabaseProvider;

    private Provider<FileRepository> fileRepositoryProvider;

    private Provider<DataStore<Preferences>> provideDataStoreProvider;

    private Provider<PreferencesManager> preferencesManagerProvider;

    private SingletonCImpl(ApplicationContextModule applicationContextModuleParam) {
      this.applicationContextModule = applicationContextModuleParam;
      initialize(applicationContextModuleParam);

    }

    private RecentDao recentDao() {
      return AppModule_ProvideRecentDaoFactory.provideRecentDao(provideDatabaseProvider.get());
    }

    @SuppressWarnings("unchecked")
    private void initialize(final ApplicationContextModule applicationContextModuleParam) {
      this.storageRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<StorageRepository>(singletonCImpl, 0));
      this.provideDatabaseProvider = DoubleCheck.provider(new SwitchingProvider<AppDatabase>(singletonCImpl, 2));
      this.fileRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<FileRepository>(singletonCImpl, 1));
      this.provideDataStoreProvider = DoubleCheck.provider(new SwitchingProvider<DataStore<Preferences>>(singletonCImpl, 4));
      this.preferencesManagerProvider = DoubleCheck.provider(new SwitchingProvider<PreferencesManager>(singletonCImpl, 3));
    }

    @Override
    public void injectCleanFilesApp(CleanFilesApp cleanFilesApp) {
    }

    @Override
    public Set<Boolean> getDisableFragmentGetContextFix() {
      return Collections.<Boolean>emptySet();
    }

    @Override
    public ActivityRetainedComponentBuilder retainedComponentBuilder() {
      return new ActivityRetainedCBuilder(singletonCImpl);
    }

    @Override
    public ServiceComponentBuilder serviceComponentBuilder() {
      return new ServiceCBuilder(singletonCImpl);
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.rahees.cleanfiles.data.StorageRepository 
          return (T) new StorageRepository(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 1: // com.rahees.cleanfiles.data.FileRepository 
          return (T) new FileRepository(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), singletonCImpl.recentDao());

          case 2: // com.rahees.cleanfiles.data.local.AppDatabase 
          return (T) AppModule_ProvideDatabaseFactory.provideDatabase(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 3: // com.rahees.cleanfiles.data.PreferencesManager 
          return (T) new PreferencesManager(singletonCImpl.provideDataStoreProvider.get());

          case 4: // androidx.datastore.core.DataStore<androidx.datastore.preferences.core.Preferences> 
          return (T) AppModule_ProvideDataStoreFactory.provideDataStore(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          default: throw new AssertionError(id);
        }
      }
    }
  }
}
