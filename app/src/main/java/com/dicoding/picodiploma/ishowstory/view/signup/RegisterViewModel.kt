import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.ishowstory.data.AuthRepository
import com.dicoding.picodiploma.ishowstory.data.Result
import com.dicoding.picodiploma.ishowstory.data.remote.response.RegisterResponse
import kotlinx.coroutines.launch

class RegisterViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _registerResult = MutableLiveData<Result<RegisterResponse>>()
    val registerResult: LiveData<Result<RegisterResponse>> = _registerResult

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            _registerResult.value = Result.Loading
            val result = repository.register(name, email, password)
            _registerResult.value = result
        }
    }
}
