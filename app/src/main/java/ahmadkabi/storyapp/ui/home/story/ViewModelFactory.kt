package ahmadkabi.storyapp.ui.home.story

import ahmadkabi.storyapp.helper.Injection
import ahmadkabi.storyapp.ui.add.AddStoryViewModel
import ahmadkabi.storyapp.ui.login.LoginViewModel
import ahmadkabi.storyapp.ui.map.MapViewModel
import ahmadkabi.storyapp.ui.register.RegisterViewModel
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(Injection.provideRepository(context)) as T
        }else if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RegisterViewModel(Injection.provideRepository(context)) as T
        }else if (modelClass.isAssignableFrom(AddStoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddStoryViewModel(Injection.provideRepository(context)) as T
        }else if (modelClass.isAssignableFrom(StoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StoryViewModel(Injection.provideRepository(context)) as T
        }else if (modelClass.isAssignableFrom(MapViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MapViewModel(Injection.provideRepository(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}