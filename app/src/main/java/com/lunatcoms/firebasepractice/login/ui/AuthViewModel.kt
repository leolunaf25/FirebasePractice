package com.lunatcoms.firebasepractice.login.ui

import android.content.Context
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.lunatcoms.firebasepractice.R

class AuthViewModel : ViewModel() {

    /*Autenticacion con Google*/

    private val _googleSignInResult = MutableLiveData<Boolean>()
    val googleSignInResult: LiveData<Boolean> = _googleSignInResult

    private val authRepository: AuthRepository = AuthRepository(FirebaseAuth.getInstance())

    fun signInWithGoogle(idToken: String) {
        authRepository.signInWithGoogle(idToken) { success, error ->
            if (success) {
                _googleSignInResult.value = true
                _navigateToHome.value = true // Navegación a Home
            } else {
                _googleSignInResult.value = false
                Log.e("GoogleSignIn", "Error: $error")
            }
        }
    }

    fun signOutGoogle(context: Context, onComplete: () -> Unit) {
        val googleSignInClient = GoogleSignIn.getClient(
            context,
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        )

        googleSignInClient.signOut().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                FirebaseAuth.getInstance().signOut()
                onComplete()
            } else {
                Log.e("GoogleSignOut", "Error al cerrar sesión con Google: ${task.exception?.message}")
            }
        }
    }


    ///Get User Data
    private val _userName = MutableLiveData<String?>()
    val userName: LiveData<String?> = _userName

    init {
        _userName.value = FirebaseAuth.getInstance().currentUser?.displayName
    }

    fun getGoogleUserName(): String? {
        return FirebaseAuth.getInstance().currentUser?.displayName
    }


    /**/


    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _loginEnable = MutableLiveData<Boolean>()
    val loginEnable: LiveData<Boolean> = _loginEnable

    private val _navigateToSignup = MutableLiveData<Boolean>()
    val navigateToSignup: LiveData<Boolean> = _navigateToSignup

    private val _navigateToHome = MutableLiveData<Boolean>()
    val navigateToHome: LiveData<Boolean> = _navigateToHome

    fun onSignupSelected() {
        _navigateToSignup.value = true
    }

    fun onLoginChanged(email: String, password: String) {
        _email.value = email
        _password.value = password
        _loginEnable.value = isValidEmail(email) && isValidPassword(password)
    }

    private fun isValidPassword(password: String): Boolean = password.length > 6
    private fun isValidEmail(email: String): Boolean =
        Patterns.EMAIL_ADDRESS.matcher(email).matches()

    fun onLoginSelected() {

        val isLoginSuccessful = true
        if (isLoginSuccessful) {
            _navigateToHome.value = true
        }
    }

    fun resetNavigation() {
        _googleSignInResult.value = false
        _navigateToHome.value = false
        _navigateToSignup.value = false
    }

    fun onAcceptSelected() {

    }


    //Authentication Firebase

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    init {
        checkAuthStatus()
    }

    private fun checkAuthStatus() {
        if (auth.currentUser == null) {
            _authState.value = AuthState.Unauthenticated
        } else {
            _authState.value = AuthState.Authenticated
        }
    }

    fun login(email: String, password: String) {

        if(email.isEmpty() || password.isEmpty()){
            _authState.value = AuthState.Error("Completa todos los campos por favor")
            return
        }

        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.Authenticated
                } else {
                    _authState.value = AuthState.Error(task.exception?.message?: "Algo salió mal")
                }
            }
    }

    fun signup(email: String, password: String) {

        if(email.isEmpty() || password.isEmpty()){
            _authState.value = AuthState.Error("Completa todos los campos por favor")
            return
        }

        _authState.value = AuthState.Loading
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.Authenticated
                } else {
                    _authState.value = AuthState.Error(task.exception?.message?: "Algo salió mal")
                }
            }
    }

    fun signout(context: Context, onComplete: () -> Unit) {
        signOutGoogle(context) {
            _authState.value = AuthState.Unauthenticated
            onComplete()
        }
    }
}

sealed class AuthState {
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    data class Error(val message: String) : AuthState()
}


class AuthRepository(private val firebaseAuth: FirebaseAuth) {
    fun signInWithGoogle(idToken: String, onResult: (Boolean, String?) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(true, null)
                } else {
                    onResult(false, task.exception?.message)
                }
            }
    }
}
