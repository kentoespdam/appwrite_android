plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.kentoes.appwrite2"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.kentoes.appwrite2"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.majorVersion
    }
}

dependencies {
    implementation("com.google.devtools.ksp:symbol-processing-api:1.9.20-1.0.14")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.9.20")

    //AndroidX core
    implementation("androidx.core:core-ktx:1.12.0")

    //AppWrite
    implementation("io.appwrite:sdk-for-android:4.0.1")

    //Image Loading
    implementation("com.github.bumptech.glide:glide:4.16.0")
    ksp("com.github.bumptech.glide:ksp:4.16.0")

    //AppCompat
    implementation("androidx.appcompat:appcompat:1.6.1")


    //LiveData
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")

    //ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")

    //Activity
    implementation("androidx.activity:activity-ktx:1.8.1")

    //Material
    implementation("com.google.android.material:material:1.10.0")

    //JSON
    implementation("com.google.code.gson:gson:2.10.1")

    //Test
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}

ksp{
    arg("option1","value1")
    arg("verbose","true")
}