import SwiftUI
import Firebase
import ComposeApp

@main
struct iOSApp: App {

    init() {
        FirebaseApp.configure()
        AppDelegateKt.doInitKoinIos()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
