package com.rahees.cleanfiles.di;

import com.rahees.cleanfiles.data.local.AppDatabase;
import com.rahees.cleanfiles.data.local.RecentDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class AppModule_ProvideRecentDaoFactory implements Factory<RecentDao> {
  private final Provider<AppDatabase> databaseProvider;

  public AppModule_ProvideRecentDaoFactory(Provider<AppDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public RecentDao get() {
    return provideRecentDao(databaseProvider.get());
  }

  public static AppModule_ProvideRecentDaoFactory create(Provider<AppDatabase> databaseProvider) {
    return new AppModule_ProvideRecentDaoFactory(databaseProvider);
  }

  public static RecentDao provideRecentDao(AppDatabase database) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideRecentDao(database));
  }
}
