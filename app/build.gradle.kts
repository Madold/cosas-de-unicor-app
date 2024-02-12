import com.android.build.gradle.internal.tasks.factory.dependsOn

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    namespace = "com.markusw.cosasdeunicorapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.markusw.cosasdeunicorapp"
        minSdk = 26
        targetSdk = 34
        versionCode = 5
        versionName = "beta-5-mary"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        val developmentVariant = "development"
        val debugVariant = "debug"
        val googleServicesJsonFile = "google-services.json"
        val debugPath = "src/debug"
        val productionPath = "src/release"
        val rootDirectory = "src/"

        //Tasks to copy the google-services.json file to the root directory
        val copyReleaseGoogleServicesJson by tasks.registering(Copy::class) {
            from(productionPath)
            include(googleServicesJsonFile)
            into(rootDirectory)
            rename { "google-services.json" }
        }

        val copyDebugGoogleServicesJson by tasks.registering(Copy::class) {
            from(debugPath)
            include(googleServicesJsonFile)
            into(rootDirectory)
            rename { "google-services.json" }
        }

        create(developmentVariant) {
            initWith(getByName(debugVariant))
        }
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            isDebuggable = false
            isShrinkResources = true

            tasks.getByName<Copy>("copyDebugGoogleServicesJson").dependsOn(":app:copyReleaseGoogleServicesJson")
        }
        debug {
            isMinifyEnabled = false
            isDebuggable = true
            isShrinkResources = false
            applicationIdSuffix = ".debug"
            tasks.getByName<Copy>("copyReleaseGoogleServicesJson").dependsOn(":app:copyDebugGoogleServicesJson")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        viewBinding = true
        compose = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    val daggerHiltVersion = "2.48"
    val flexboxVersion = "3.0.0"
    val timberVersion = "5.0.1"
    val mockkVersion = "1.13.5"
    val coroutinesTestVersion = "1.7.1"
    val loggerVersion = "2.2.0"
    val gmsAuthServicesVersion = "20.7.0"
    val splashScreenVersion = "1.0.1"
    val navigationComposeVersion = "2.7.6"
    val hiltNavigationComposeVersion = "1.1.0"
    val lifecycleComposeVersion = "2.7.0"
    val coilComposeVersion = "2.4.0"
    val composeBomVersion = "2023.10.01"
    val playServicesAdsVersion = "22.6.0"
    val retrofitVersion = "2.9.0"
    val paletteVersion = "1.0.0"
    val roomVersion = "2.5.0"
    val dataStoreVersion = "1.0.0"
    val lottieVersion = "6.3.0"
    val animatedNavigationBarVersion = "1.0.0"

    //App dependencies
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.6")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.6")
    implementation(platform("androidx.compose:compose-bom:$composeBomVersion"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

    //Architecture components
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")

    //Dagger hilt
    implementation("com.google.dagger:hilt-android:$daggerHiltVersion")
    ksp("com.google.dagger:hilt-android-compiler:$daggerHiltVersion")

    //Flexbox layout
    implementation("com.google.android.flexbox:flexbox:$flexboxVersion")

    //Firebase
    implementation(platform("com.google.firebase:firebase-bom:32.3.1"))
    //Analytics
    implementation("com.google.firebase:firebase-analytics-ktx")
    //Auth
    implementation("com.google.firebase:firebase-auth-ktx")
    //Storage
    implementation("com.google.firebase:firebase-storage-ktx")
    //FireStore
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.google.firebase:firebase-messaging-ktx")

    //GMS Play Services Auth
    implementation("com.google.android.gms:play-services-auth:$gmsAuthServicesVersion")

    //Play services ads
    implementation("com.google.android.gms:play-services-ads:$playServicesAdsVersion")

    //Timber
    implementation("com.jakewharton.timber:timber:$timberVersion")

    //Logger
    implementation("com.orhanobut:logger:$loggerVersion")

    //Splash Screen API
    implementation("androidx.core:core-splashscreen:$splashScreenVersion")

    //Navigation Compose
    implementation("androidx.navigation:navigation-compose:$navigationComposeVersion")

    //Lifecycle compose
    implementation("androidx.lifecycle:lifecycle-runtime-compose:$lifecycleComposeVersion")

    //Hilt navigation Compose
    implementation("androidx.hilt:hilt-navigation-compose:$hiltNavigationComposeVersion")

    //Coil compose
    implementation("io.coil-kt:coil-compose:$coilComposeVersion")

    //Retrofit
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")

    //Animated Navigation Bar
    implementation("com.exyte:animated-navigation-bar:$animatedNavigationBarVersion")

    //Firebase crashlytics
    implementation("com.google.firebase:firebase-crashlytics-ktx")

    //Palette
    implementation("androidx.palette:palette:$paletteVersion")

    //Room
    implementation("androidx.room:room-ktx:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")

    //DataStore
    implementation("androidx.datastore:datastore-preferences:$dataStoreVersion")

    //Lottie
    //Lottie compose
    implementation("com.airbnb.android:lottie-compose:$lottieVersion")


    //Test dependencies
    testImplementation("junit:junit:4.13.2")
    //MockK
    testImplementation("io.mockk:mockk:$mockkVersion")
    //Kotlin coroutines test
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesTestVersion")

    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:$composeBomVersion"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
