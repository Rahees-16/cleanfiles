package com.rahees.cleanfiles.ui.browser;

import com.rahees.cleanfiles.data.FileRepository;
import com.rahees.cleanfiles.data.PreferencesManager;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
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
public final class BrowserViewModel_Factory implements Factory<BrowserViewModel> {
  private final Provider<FileRepository> fileRepositoryProvider;

  private final Provider<PreferencesManager> preferencesManagerProvider;

  public BrowserViewModel_Factory(Provider<FileRepository> fileRepositoryProvider,
      Provider<PreferencesManager> preferencesManagerProvider) {
    this.fileRepositoryProvider = fileRepositoryProvider;
    this.preferencesManagerProvider = preferencesManagerProvider;
  }

  @Override
  public BrowserViewModel get() {
    return newInstance(fileRepositoryProvider.get(), preferencesManagerProvider.get());
  }

  public static BrowserViewModel_Factory create(Provider<FileRepository> fileRepositoryProvider,
      Provider<PreferencesManager> preferencesManagerProvider) {
    return new BrowserViewModel_Factory(fileRepositoryProvider, preferencesManagerProvider);
  }

  public static BrowserViewModel newInstance(FileRepository fileRepository,
      PreferencesManager preferencesManager) {
    return new BrowserViewModel(fileRepository, preferencesManager);
  }
}
