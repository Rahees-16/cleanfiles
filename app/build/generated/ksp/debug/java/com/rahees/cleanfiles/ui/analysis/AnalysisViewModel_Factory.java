package com.rahees.cleanfiles.ui.analysis;

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
public final class AnalysisViewModel_Factory implements Factory<AnalysisViewModel> {
  private final Provider<StorageRepository> storageRepositoryProvider;

  public AnalysisViewModel_Factory(Provider<StorageRepository> storageRepositoryProvider) {
    this.storageRepositoryProvider = storageRepositoryProvider;
  }

  @Override
  public AnalysisViewModel get() {
    return newInstance(storageRepositoryProvider.get());
  }

  public static AnalysisViewModel_Factory create(
      Provider<StorageRepository> storageRepositoryProvider) {
    return new AnalysisViewModel_Factory(storageRepositoryProvider);
  }

  public static AnalysisViewModel newInstance(StorageRepository storageRepository) {
    return new AnalysisViewModel(storageRepository);
  }
}
