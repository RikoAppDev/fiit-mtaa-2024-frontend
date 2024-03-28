package core.data.remote


sealed class UrlHelper(val path: String) {
    data object BaseUrl : UrlHelper("http://192.168.1.26:3000")

    data object CreateAccountUrl : UrlHelper("createAccount/")
    data object LoginUserUrl : UrlHelper("login/")
}