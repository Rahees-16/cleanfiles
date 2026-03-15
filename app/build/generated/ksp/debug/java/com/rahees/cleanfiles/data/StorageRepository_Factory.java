package com.rahees.cleanfiles.data;

import android.content.Context;
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
public final class StorageRepository_Factory implements Factory<StorageRepository> {
  private final Provider<Context> contextProvider;

  public StorageRepository_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public StorageRepository get() {
    return newInstance(contextProvider.get());
  }

  public static StorageRepository_Factory create(Provider<Context> contextProvider) {
    return new StorageRepository_Factory(contextProvider);
  }

  public static StorageRepository newInstance(Context context) {
    return new StorageRepository(context);
  }
}
