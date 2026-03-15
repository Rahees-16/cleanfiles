package com.rahees.cleanfiles.ui.home;

import com.rahees.cleanfiles.data.FileRepository;
import com.rahees.cleanfiles.data.StorageRepository;
import com.rahees.cleanfiles.data.local.RecentDao;
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
public final class HomeViewModel_Factory implements Factory<HomeViewModel> {
  private final Provider<StorageRepository> storageRepositoryProvider;

  private final Provider<FileRepository> fileRepositoryProvider;

  private final Provider<RecentDao> recentDaoProvider;

  public HomeViewModel_Factory(Provider<StorageRepository> storageRepositoryProvider,
      Provider<FileRepository> fileRepositoryProvider, Provider<RecentDao> recentDaoProvider) {
    this.storageRepositoryProvider = storageRepositoryProvider;
    this.fileRepositoryProvider = fileRepositoryProvider;
    this.recentDaoProvider = recentDaoProvider;
  }

  @Override
  public HomeViewModel get() {
    return newInstance(storageRepositoryProvider.get(), fileRepositoryProvider.get(), recentDaoProvider.get());
  }

  public static HomeViewModel_Factory create(Provider<StorageRepository> storageRepositoryProvider,
      Provider<FileRepository> fileRepositoryProvider, Provider<RecentDao> recentDaoProvider) {
    return new HomeViewModel_Factory(storageRepositoryProvider, fileRepositoryProvider, recentDaoProvider);
  }

  public static HomeViewModel newInstance(StorageRepository storageRepository,
      FileRepository fileRepository, RecentDao recentDao) {
    return new HomeViewModel(storageRepository, fileRepository, recentDao);
  }
}
