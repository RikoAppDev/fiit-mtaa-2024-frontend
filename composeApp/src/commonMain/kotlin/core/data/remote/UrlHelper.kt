package core.data.remote


sealed class UrlHelper(val path: String) {
    data object BaseUrl : UrlHelper("http://192.168.191.181:3000/")

    data object CreateAccountUrl : UrlHelper("createAccount/")
    data object LoginUserUrl : UrlHelper("login/")
}