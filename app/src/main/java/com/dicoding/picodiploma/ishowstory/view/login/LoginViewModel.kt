import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.ishowstory.data.AuthRepository
import com.dicoding.picodiploma.ishowstory.data.Result
import com.dicoding.picodiploma.ishowstory.data.remote.response.LoginResponse
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _loginResult = MutableLiveData<Result<LoginResponse>>()
    val loginResult: LiveData<Result<LoginResponse>> = _loginResult

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginResult.value = Result.Loading
            val result = repository.login(email, password)
            _loginResult.value = result
        }
    }
}
