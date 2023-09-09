plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.markusw.cosasdeunicorapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.markusw.cosasdeunicorapp"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            isDebuggable = false
            isShrinkResources = true
        }
        debug {
            isMinifyEnabled = false
            isDebuggable = true
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
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    val daggerHiltVersion = "2.44"
    val flexboxVersion = "3.0.0"
    val timberVersion = "5.0.1"
    val mockkVersion = "1.13.5"
    val coroutinesTestVersion = "1.7.1"
    val loggerVersion = "2.2.0"
    val gmsAuthServicesVersion = "20.7.0"
    val splashScreenVersion = "1.0.1"
    val navigationComposeVersion = "2.7.1"
    val hiltNavigationComposeVersion = "1.0.0"
    val lifecycleComposeVersion = "2.6.1"
    val coilComposeVersion = "2.4.0"

    //App dependencies
    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.1")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.1")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

    //Architecture components
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.activity:activity-compose:1.7.2")

    //Dagger hilt
    implementation("com.google.dagger:hilt-android:$daggerHiltVersion")
    kapt("com.google.dagger:hilt-android-compiler:$daggerHiltVersion")

    //Flexbox layout
    implementation("com.google.android.flexbox:flexbox:$flexboxVersion")

    //Firebase
    implementation(platform("com.google.firebase:firebase-bom:32.2.2"))
    //Analytics
    implementation("com.google.firebase:firebase-analytics-ktx")
    //Auth
    implementation("com.google.firebase:firebase-auth-ktx")
    //Storage
    implementation("com.google.firebase:firebase-storage-ktx")
    //FireStore
    implementation("com.google.firebase:firebase-firestore-ktx")

    //GMS Play Services Auth
    implementation("com.google.android.gms:play-services-auth:$gmsAuthServicesVersion")

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

    //Test dependencies
    testImplementation("junit:junit:4.13.2")
    //MockK
    testImplementation("io.mockk:mockk:$mockkVersion")
    //Kotlin coroutines test
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesTestVersion")

    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
// Allow references to generated code
kapt {
    correctErrorTypes = true
}
