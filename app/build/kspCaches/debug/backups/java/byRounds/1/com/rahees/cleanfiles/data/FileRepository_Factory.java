package com.rahees.cleanfiles.data;

import android.content.Context;
import com.rahees.cleanfiles.data.local.RecentDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class FileRepository_Factory implements Factory<FileRepository> {
  private final Provider<Context> contextProvider;

  private final Provider<RecentDao> recentDaoProvider;

  public FileRepository_Factory(Provider<Context> contextProvider,
      Provider<RecentDao> recentDaoProvider) {
    this.contextProvider = contextProvider;
    this.recentDaoProvider = recentDaoProvider;
  }

  @Override
  public FileRepository get() {
    return newInstance(contextProvider.get(), recentDaoProvider.get());
  }

  public static FileRepository_Factory create(Provider<Context> contextProvider,
      Provider<RecentDao> recentDaoProvider) {
    return new FileRepository_Factory(contextProvider, recentDaoProvider);
  }

  public static FileRepository newInstance(Context context, RecentDao recentDao) {
    return new FileRepository(context, recentDao);
  }
}
