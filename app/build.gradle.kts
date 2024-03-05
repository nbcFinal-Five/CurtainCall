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
		versionCode = 1
		versionName = "1.0"

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
		buildConfigField("String", "KOPIS_API_KEY", getAuthKey("KOPIS_API_KEY"))
		buildConfigField("String", "SUPABASE_KEY", getAuthKey("SUPABASE_KEY"))
		buildConfigField("String", "SUPABASE_SERVICE_ROLE", getAuthKey("SUPABASE_SERVICE_ROLE"))
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

	//coil 라이브러리
	implementation("io.coil-kt:coil:2.4.0")

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

	// tikxml 0.8.15 -> 그래들 충돌 문제
	implementation("com.tickaroo.tikxml:annotation:0.8.13")
	implementation("com.tickaroo.tikxml:core:0.8.13")

	kapt("com.tickaroo.tikxml:processor:0.8.13")
	implementation("com.tickaroo.tikxml:retrofit-converter:0.8.13")

	// Coroutine
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

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

	// Kakao map
	implementation("com.kakao.maps.open:android:2.6.0")

	//BottomSheet, Chip
	implementation("com.google.android.material:material:1.11.0")

	//Glide
	implementation("com.github.bumptech.glide:glide:4.16.0")
	annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")

	// shimmer
	implementation("com.facebook.shimmer:shimmer:0.5.0")
}