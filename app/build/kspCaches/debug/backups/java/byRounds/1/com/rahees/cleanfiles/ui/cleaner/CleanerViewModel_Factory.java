package com.rahees.cleanfiles.ui.cleaner;

import com.rahees.cleanfiles.data.StorageRepository;
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
public final class CleanerViewModel_Factory implements Factory<CleanerViewModel> {
  private final Provider<StorageRepository> storageRepositoryProvider;

  public CleanerViewModel_Factory(Provider<StorageRepository> storageRepositoryProvider) {
    this.storageRepositoryProvider = storageRepositoryProvider;
  }

  @Override
  public CleanerViewModel get() {
    return newInstance(storageRepositoryProvider.get());
  }

  public static CleanerViewModel_Factory create(
      Provider<StorageRepository> storageRepositoryProvider) {
    return new CleanerViewModel_Factory(storageRepositoryProvider);
  }

  public static CleanerViewModel newInstance(StorageRepository storageRepository) {
    return new CleanerViewModel(storageRepository);
  }
}
