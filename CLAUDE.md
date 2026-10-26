# CLAUDE.md

Proyecto Kotlin Multiplatform (Android + iOS) con Compose Multiplatform. Paquete base: `org.ucb.appp1`.

## Reglas del proyecto

- **Autoría de git:** el único autor y colaborador es el usuario (Mateo Quintanilla). No agregar líneas `Co-Authored-By`, ni "Generated with Claude Code", ni ninguna mención a Claude en commits, PRs o código.
- No modificar la arquitectura existente; agregar solo lo necesario, de la forma más simple posible.
- Seguir el estilo del código vecino (nombres, comentarios mínimos, idioma).
- Antes de hacer push o acciones externas, hacerlo solo cuando el usuario lo pida.
- Convención de ramas: `feature/<nombre>`.

## Estructura y arquitectura

- Módulos: `androidApp` (app Android, `MainApplication`, `MainActivity`) y `shared` (lógica y UI KMP). `iosApp` contiene el proyecto iOS.
- Cada feature se organiza en `data` (`datasource`, `dto`, `mapper`, `repository`, `service`), `domain` (`model`, `repository`, `usecase`) y `presentation` (`screen`, `viewmodel`).
- Features actuales: `signin`, `userinformation` (API GitHub), `crossref`, `movies`, `exchange` (Realtime Database + Remote Config).
- Inyección con Koin: módulos en `shared/.../di` (`DataModule`, `DomainModule`, `PresentationModule`, `SharedModules`). Android inicia Koin con `initKoinAndroid` en `MainApplication`.
- Casos de uso: clase con `suspend fun invoke()`, registrados con `singleOf`. ViewModels con `viewModelOf`.
- Análisis estático: Detekt (`config/detekt/detekt.yml`, tarea `detektAll`).

## Dependencias clave (`gradle/libs.versions.toml`)

- Kotlin 2.4.10, AGP 9.0.1, Compose Multiplatform 1.11.1, Koin 4.2.2, Ktor 3.4.1.
- GitLive Firebase KMP 2.7.0: `firebase-database` y `firebase-config` (en `commonMain`).
- WorkManager `androidx.work:work-runtime-ktx` 2.11.0 (solo `androidMain`; rama `feature/cambios-en-segundo-plano`).
- Plugin `googleServices` en `androidApp`; `androidApp/google-services.json` (proyecto Firebase `prueba-2051a`).

## Actualizaciones realizadas

### Tareas en segundo plano (rama `feature/cambios-en-segundo-plano`)
- `commonMain/.../scheduler/LogScheduler.kt`: `expect class LogScheduler()` con `schedulerPeriodicalUpload()`.
- `androidMain/.../scheduler/LogScheduler.android.kt`: `PeriodicWorkRequest` cada 15 min con `NetworkType.CONNECTED`, `enqueueUniquePeriodicWork("log_upload_work", KEEP, ...)`. El `Context` se obtiene con `KoinPlatform.getKoin().get()` (no heredar de `KoinComponent`: `androidApp` no ve Koin Core).
- `androidMain/.../scheduler/LogUploadWorker.kt`: `CoroutineWorker` que imprime un mensaje y devuelve `Result.success()`.
- `iosMain/.../scheduler/LogScheduler.ios.kt`: no-op.
- `MainApplication` llama a `LogScheduler().schedulerPeriodicalUpload()` tras iniciar Koin.

### Realtime Database (rama `feature/databaserealtime`)
- `exchange/data/datasource/RealTimeDataBase.kt`: observa el nodo `app_dollar` con `Firebase.database` y `valueEvents`.

### Remote Config (rama `feature/remoteconfig`)
- `exchange/data/datasource/RemoteConfigDataSource.kt`: usa `Firebase.remoteConfig`, `setDefaults`, `fetchAndActivate()`. Parámetros String: `contact_phone` y `contact_email`. Intervalo mínimo de fetch en 0 s (solo para pruebas; subirlo a 1 h o más en producción).
- `exchange/domain/model/ContactInfo.kt`, `GetContactInfoUseCase.kt`, y `getContactInfo()` en `ExchangeRepository`/`ExchangeRepositoryImpl`.
- `ExchangeViewModel` expone `contact`; `ExchangeScreen` muestra teléfono, email y el mensaje de la base de datos.
- Los parámetros se crean y publican en Firebase Console → Remote Config.

### Firestore (rama `feature/firestore`)
- Dependencia `dev.gitlive:firebase-firestore` 2.7.0 (`libs.firebase.firestore`, `commonMain`).
- Feature `notes` (colección `notes`, campos `title` y `description`): `FirestoreDataSource` (`snapshots` en tiempo real, `add`, `delete`), `NoteDto` (`@Serializable`), `NoteModel`, `NoteRepository`/`NoteRepositoryImpl`, casos de uso `ObserveNotesUseCase`, `AddNoteUseCase`, `DeleteNoteUseCase`, `NotesViewModel` y `NotesScreen` (formulario, lista y botón eliminar).
- `App.kt` muestra `ExchangeScreen` (Remote Config + Realtime Database) arriba y `NotesScreen` (Firestore) abajo.
- En Firebase Console hay que crear la base Firestore (Build → Firestore Database) y permitir lectura/escritura en las reglas para pruebas.

### Notificaciones push - FCM (Android)
- El token se obtiene con GitLive (`dev.gitlive:firebase-messaging` 2.7.0) en la feature `notifications` de `shared` (datasource, repositorio, `GetFcmTokenUseCase`, `NotificationsViewModel`, `NotificationsScreen`). La recepción en Android usa el SDK nativo `firebase-messaging` con `firebase-bom` 34.18.0 en `androidApp`.
- `androidApp/.../push/AppFirebaseMessagingService.kt`: `onNewToken` (log) y `onMessageReceived` (muestra notificación local con el canal `default_channel` cuando la app está en primer plano).
- `AndroidManifest.xml`: permiso `POST_NOTIFICATIONS`, el `service` con `MESSAGING_EVENT`, el canal y el icono por defecto.
- `MainActivity` pide el permiso (Android 13+). El token se muestra en `NotificationsScreen` (mantener presionado para copiar).
- Prueba: Firebase Console → Messaging → New campaign → Notification → "Send test message" y pegar el token de la pantalla.

## Pendientes conocidos

- iOS: falta `GoogleService-Info.plist`, inicialización de Firebase y enlazar el SDK nativo (CocoaPods o SPM).

## Comandos

- Compilar shared (Android): `./gradlew :shared:compileAndroidMain`.
- En esta máquina no hay Java en el PATH: usar `JAVA_HOME="C:\Program Files\Android\Android Studio\jbr"`.
- Análisis estático: `./gradlew :shared:detektAll`.
