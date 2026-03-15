package com.rahees.cleanfiles.ui.category;

import com.rahees.cleanfiles.data.FileRepository;
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
public final class CategoryViewModel_Factory implements Factory<CategoryViewModel> {
  private final Provider<FileRepository> fileRepositoryProvider;

  public CategoryViewModel_Factory(Provider<FileRepository> fileRepositoryProvider) {
    this.fileRepositoryProvider = fileRepositoryProvider;
  }

  @Override
  public CategoryViewModel get() {
    return newInstance(fileRepositoryProvider.get());
  }

  public static CategoryViewModel_Factory create(Provider<FileRepository> fileRepositoryProvider) {
    return new CategoryViewModel_Factory(fileRepositoryProvider);
  }

  public static CategoryViewModel newInstance(FileRepository fileRepository) {
    return new CategoryViewModel(fileRepository);
  }
}
