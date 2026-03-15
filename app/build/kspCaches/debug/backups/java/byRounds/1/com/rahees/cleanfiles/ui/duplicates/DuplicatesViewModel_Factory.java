package com.rahees.cleanfiles.ui.duplicates;

import com.rahees.cleanfiles.data.FileRepository;
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
public final class DuplicatesViewModel_Factory implements Factory<DuplicatesViewModel> {
  private final Provider<StorageRepository> storageRepositoryProvider;

  private final Provider<FileRepository> fileRepositoryProvider;

  public DuplicatesViewModel_Factory(Provider<StorageRepository> storageRepositoryProvider,
      Provider<FileRepository> fileRepositoryProvider) {
    this.storageRepositoryProvider = storageRepositoryProvider;
    this.fileRepositoryProvider = fileRepositoryProvider;
  }

  @Override
  public DuplicatesViewModel get() {
    return newInstance(storageRepositoryProvider.get(), fileRepositoryProvider.get());
  }

  public static DuplicatesViewModel_Factory create(
      Provider<StorageRepository> storageRepositoryProvider,
      Provider<FileRepository> fileRepositoryProvider) {
    return new DuplicatesViewModel_Factory(storageRepositoryProvider, fileRepositoryProvider);
  }

  public static DuplicatesViewModel newInstance(StorageRepository storageRepository,
      FileRepository fileRepository) {
    return new DuplicatesViewModel(storageRepository, fileRepository);
  }
}
