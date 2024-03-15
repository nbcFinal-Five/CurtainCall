import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
	id("com.android.application")
	id("org.jetbrains.kotlin.android")
	id("kotlin-kapt")
	kotlin("plugin.serialization") version "1.9.22"
}

android {
	namespace = "com.nbc.curtaincall"
	compileSdk = 34

	defaultConfig {
		applicationId = "com.nbc.curtaincall"
		minSdk = 26
		targetSdk = 34
		versionCode = 2
		versionName = "1.1"

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
		buildConfigField("String", "KOPIS_API_KEY", getAuthKey("KOPIS_API_KEY"))
		buildConfigField("String", "SUPABASE_KEY", getAuthKey("SUPABASE_KEY"))
		buildConfigField("String", "SUPABASE_SERVICE_ROLE", getAuthKey("SUPABASE_SERVICE_ROLE"))
		buildConfigField("String","MAP_CLIENT_ID",getAuthKey("MAP_CLIENT_ID"))
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
		sourceCompatibility = JavaVersion.VERSION_1_8
		targetCompatibility = JavaVersion.VERSION_1_8
	}
	kotlinOptions {
		jvmTarget = "1.8"
	}
	buildFeatures {
		viewBinding = true
		buildConfig = true
	}
}
fun getAuthKey(propertyKey: String): String =
	gradleLocalProperties(rootDir).getProperty(propertyKey)
dependencies {
	// glide 
	implementation ("com.github.bumptech.glide:glide:4.16.0")
	annotationProcessor ("com.github.bumptech.glide:compiler:4.16.0")

	// glide blur transformation
	implementation ("jp.wasabeef:glide-transformations:4.3.0")
	implementation ("jp.co.cyberagent.android:gpuimage:2.1.0")

	// navigation
	implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
	implementation("androidx.navigation:navigation-ui-ktx:2.7.7")

	// by viewModels()
	implementation("androidx.fragment:fragment-ktx:1.6.2")
	implementation("androidx.activity:activity-ktx:1.8.2")

	// retrofit2
	implementation("com.squareup.retrofit2:retrofit:2.9.0")
	implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
	implementation("com.squareup.retrofit2:converter-gson:2.9.0")
	implementation("com.google.code.gson:gson:2.10.1")

	// simpleXml
	implementation ("org.simpleframework:simple-xml:2.7.1")
	implementation ("com.squareup.retrofit2:converter-simplexml:2.9.0")
	// Coroutine
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")

	// Lifecycle components
	implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
	implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
	implementation("androidx.lifecycle:lifecycle-common-java8:2.7.0")
	implementation("androidx.lifecycle:lifecycle-runtime-ktx:r2.6.0")

	//powerspinner
	implementation("com.github.skydoves:powerspinner:1.2.6")

	implementation("androidx.core:core-ktx:1.12.0")
	implementation("androidx.appcompat:appcompat:1.6.1")
	implementation("com.google.android.material:material:1.11.0")
	implementation("androidx.constraintlayout:constraintlayout:2.1.4")
	testImplementation("junit:junit:4.13.2")
	androidTestImplementation("androidx.test.ext:junit:1.1.5")
	androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

	// supabase
	implementation(platform("io.github.jan-tennert.supabase:bom:2.1.5"))
	implementation("io.github.jan-tennert.supabase:postgrest-kt")
	implementation("io.ktor:ktor-client-android:2.3.8")

	// Naver Map
	implementation("com.naver.maps:map-sdk:3.17.0")

	// shimmer
	implementation("com.facebook.shimmer:shimmer:0.5.0")

	// lottie
	implementation ("com.airbnb.android:lottie:6.3.0")

	// android splash
	implementation("androidx.core:core-splashscreen:1.0.1")

	//MPAndroidChart
	implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

}