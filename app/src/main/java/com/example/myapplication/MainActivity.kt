package com.example.myapplication

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Checkbox
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.viewModel.FingerUiState
import com.example.myapplication.viewModel.FingerViewModel
import com.example.myapplication.viewModel.SentGesture
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory




class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    myApp()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Greeting(
    name: String, viewModel: FingerViewModel,
    modifier: Modifier = Modifier) {

    val uiState = viewModel.uiState.collectAsState()
    val gesture = uiState.value
    var answerCode = remember { mutableStateOf<Int?>(0) }

    val alert = remember { mutableStateOf(false) }
    val checkbox = viewModel.mode.value

    //val baseUrl = "http://192.168.43.51/"
    var url = ""
    val ip_adres = viewModel.ip_Adress.collectAsState().value
    if (ip_adres.isNotEmpty()) {
        url = "http://$ip_adres"
    } else {
        url = "http://null_adress"
    }
    val testUrl = "http://dummyjson.com/"
    val baseUrl = "http://192.168.0.103"
    val baseUrl1 = "http://192.168.0.104"
    val api = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        myTextField(viewModel, uiState, number = 1)
        Spacer(Modifier.height(16.dp))
        myTextField(viewModel,uiState, number = 2)
        Spacer(Modifier.height(16.dp))
        myTextField(viewModel, uiState,number = 3)
        Spacer(Modifier.height(16.dp))
        myTextField(viewModel, uiState,number = 4)
        Spacer(Modifier.height(16.dp))
        myTextField(viewModel, uiState,number = 5)
        Spacer(Modifier.height(45.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 45.dp), horizontalArrangement = Arrangement.End
        ) {


            Spacer(modifier = Modifier.weight(1f))
            Row{
                Checkbox(checked = checkbox, onCheckedChange = {viewModel.updateCheckbox()})
                Column {
                Spacer(modifier = Modifier.size(20.dp))
                Text(text = "Управление ЭМГ")
                }
            }
            Spacer(Modifier.weight(1f))
            Button(onClick = {
                val sentGesture = SentGesture(
                    uiState.value.fingerOne,
                    uiState.value.fingerTwo,
                    uiState.value.fingerThree,
                    uiState.value.fingerFour,
                    uiState.value.fingerFive,
                    checkbox
                )
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val poster = api.postGesture(sentGesture)
                        Log.i("Answer", "Result =$poster")
                        answerCode.value = poster.code()
                        Log.i("Answer", "code = $answerCode")
                        if (answerCode.value != 200) {
                            alert.value = true
                        }
                        CoroutineScope(Dispatchers.IO).cancel()
                    } catch (e: Exception) {
                        answerCode.value = null
                        alert.value = true
                    }

                }

            },
                modifier = Modifier
                    .height(60.dp)
                    .weight(2f)) {
                Icon(Icons.Sharp.Send, contentDescription = null,modifier = Modifier.size(36.dp))
            }
            if (alert.value) {
                AlertDialog(onDismissRequest = { alert.value = false;answerCode.value = 0 },
                    title = { Text(text = "Ошибка") },
                    text = {
                        var texter = "";
                        if (answerCode.value == null) {
                            texter =
                                "Нет ответа. Возможно, протез выключен или отсутствует WiFi-соединение"
                        } else {
                            texter = "Запрос не был отправлен, код: ${alert.value}"
                        }
                        Text(texter)
                    },
                    confirmButton = {
                        Button({ alert.value = false; answerCode.value = 0 }) {
                            Text("OK", fontSize = 22.sp)
                        }
                    })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun myApp(
    viewModel: FingerViewModel = viewModel(
        factory = FingerViewModel.Factory
    )
){
    val navController = rememberNavController()
    val items = listOf(
        BottomNavItem.Send,
        BottomNavItem.Lister,
        BottomNavItem.IP
    )
    val context = LocalContext.current




    Scaffold (
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomNavigation{
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach{ item ->
                    BottomNavigationItem(
                        selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                        onClick = {
                                  navController.navigate(item.route){
                                      popUpTo(navController.graph.findStartDestination().id) {
                                          saveState = true
                                      }
                                      launchSingleTop = true
                                      restoreState = true
                                  }

                                  },
                        icon = { Icon(painter = painterResource(item.icon), contentDescription = null, modifier = Modifier.size(36.dp))})

                }

            }
        }
    ){innerPadding ->
    NavHost(navController , startDestination = "Send", modifier = Modifier.padding(innerPadding)){
        composable("Send"){entry ->
            Greeting(name = "Hello Android",viewModel)
        }
        composable("List"){entry ->
            Lister(viewModel,navController)
        }
        composable("Ip"){entry ->
            ipAdres(viewModel)
        }
    }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun myTextField(
    viewModel: FingerViewModel,
    fingerUiState: State<FingerUiState>, number: Int, modifier: Modifier = Modifier){
    var isError: Boolean by remember{ mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val uiState = fingerUiState.value
    TextField(
        value = when(number)
        {
            1->uiState.fingerOne.toString()
            2->uiState.fingerTwo.toString()
            3->uiState.fingerThree.toString()
            4->uiState.fingerFour.toString()
            5->uiState.fingerFive.toString()
            else -> {""}
        },
        onValueChange = {
            if (it.isNullOrEmpty()){
                viewModel.updateTextField("0", number)
            }
            else if (it.isDigitsOnly())
            {
                viewModel.updateTextField(it,number)
                isError = false
            }
            else
            {
                isError = true;
            }
        },
        label = { Text(
            text = when (number) {
                1 -> "Большой"
                2 -> "Указательный"
                3 -> "Средний"
                4 -> "Безымянный"
                5 -> "Мизинец"
                else -> {""}
            }
        )
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = {focusManager.clearFocus()}),
        isError = isError,
        supportingText = {
            if (isError){
                Text(text = "Error")
            }
        })
}

@Composable
fun Lister(
    viewModel: FingerViewModel,
    navController: NavController
)
{
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ){
        items(gestures){it ->
            Image(painter = painterResource(id = it.picture),contentScale = ContentScale.FillHeight, contentDescription = null,
                modifier = Modifier
                    .aspectRatio(1f)
                    .fillMaxSize()
                    .clickable
                    {
                        viewModel.pictureUiUpdate(it.state)
                        navController.navigate("Send")

                    }

            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ipAdres(fingerViewModel: FingerViewModel){


    val ip_adres = fingerViewModel.ip_Adress.collectAsState().value
    val text = remember{mutableStateOf(ip_adres)}
    val url= "http://$ip_adres"
    val focusManager = LocalFocusManager.current

    Column (modifier = Modifier.fillMaxSize(),verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
    ){
        TextField(value = text.value,
            onValueChange = {
                text.value = it
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {focusManager.clearFocus()}),
            modifier = Modifier.padding(16.dp)
        )
        Text(text = "Сохраненный адрес: $url")
        Row (modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp), horizontalArrangement = Arrangement.End){
            Button(
                onClick = { fingerViewModel.saveIp(text.value) },
                modifier = Modifier
            ) {
                Text(text = "Сохранить")
            }
        }
    }

}


/*@Composable
inline fun <reified T: ViewModel> NavBackStackEntry.sharedViewModel(
    navController: NavController,
): T{
    val navGraphRoute = destination.parent?.route ?: return viewModel()
    val parentEntry = remember(this){
        navController.getBackStackEntry(navGraphRoute)
    }
    return viewModel(parentEntry)

}
*/
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {

    }
}