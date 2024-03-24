package com.example.tictactoe

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.example.tictactoe.databinding.ActivityGameScreenBinding

class GameScreen : AppCompatActivity() {
    private lateinit var dataBinding: ActivityGameScreenBinding
    private var player: Boolean = false
    private var win = false
    private var singlePlayer=false
    private var multiPlayer=false
    private var totalPlayedRound=0
    private var starWinCount=0
    private var planetWinCount=0
    private val entertainedComponents: MutableList<PlayerTurnModel> = arrayListOf()

    private var myIcon: Boolean = false
    private var draw: Boolean = false

    data class PlayerTurnModel(
        val player: Boolean,
        val id: Int
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        dataBinding= DataBindingUtil.setContentView(this@GameScreen,R.layout.activity_game_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        singlePlayer=intent.getBooleanExtra("singlePlayer",false)
        multiPlayer=intent.getBooleanExtra("multiPlayer",false)

        dataBinding.restartBtn.setOnClickListener {
            restart()
        }

        dataBinding.nextRoundBtn.setOnClickListener {
            nextRound()
        }

        dataBinding.playerSwitch.setOnClickListener {
            switchPlayer()
            if (singlePlayer) myIcon=player
        }

        dataBinding.center.setOnClickListener {
            handlePlayerInput(dataBinding.center)
        }

        dataBinding.leftCenter.setOnClickListener {
            handlePlayerInput(dataBinding.leftCenter)
        }

        dataBinding.rightCenter.setOnClickListener {
            handlePlayerInput(dataBinding.rightCenter)
        }

        dataBinding.topCenter.setOnClickListener {
            handlePlayerInput(dataBinding.topCenter)
        }

        dataBinding.bottomCenter.setOnClickListener {
            handlePlayerInput(dataBinding.bottomCenter)
        }

        dataBinding.topLeft.setOnClickListener {
            handlePlayerInput(dataBinding.topLeft)
        }

        dataBinding.topRight.setOnClickListener {
            handlePlayerInput(dataBinding.topRight)
        }

        dataBinding.bottomLeft.setOnClickListener {
            handlePlayerInput(dataBinding.bottomLeft)
        }

        dataBinding.bottomRight.setOnClickListener {
            handlePlayerInput(dataBinding.bottomRight)
        }
    }

    private fun nextRound() {
        dataBinding.center.background = AppCompatResources.getDrawable(this, R.drawable.slot_background)
        dataBinding.leftCenter.background = AppCompatResources.getDrawable(this, R.drawable.slot_background)
        dataBinding.rightCenter.background = AppCompatResources.getDrawable(this, R.drawable.slot_background)
        dataBinding.topCenter.background = AppCompatResources.getDrawable(this, R.drawable.slot_background)
        dataBinding.bottomCenter.background = AppCompatResources.getDrawable(this, R.drawable.slot_background)
        dataBinding.topLeft.background = AppCompatResources.getDrawable(this, R.drawable.slot_background)
        dataBinding.topRight.background = AppCompatResources.getDrawable(this, R.drawable.slot_background)
        dataBinding.bottomLeft.background = AppCompatResources.getDrawable(this, R.drawable.slot_background)
        dataBinding.bottomRight.background = AppCompatResources.getDrawable(this, R.drawable.slot_background)
        entertainedComponents.clear()
        win=false
        draw=false
        dataBinding.nextRoundBtn.visibility=View.INVISIBLE
        if (multiPlayer)dataBinding.playerSwitch.visibility= View.VISIBLE
        if (singlePlayer){
            player=myIcon
            if (player){
                dataBinding.currentPlayerPlanetColor.visibility= View.INVISIBLE
                dataBinding.currentPlayerPlanet.visibility= View.INVISIBLE
                dataBinding.currentPlayerStarColor.visibility= View.VISIBLE
                dataBinding.currentPlayerStar.visibility= View.VISIBLE
            }
            else{
                dataBinding.currentPlayerPlanetColor.visibility= View.VISIBLE
                dataBinding.currentPlayerPlanet.visibility= View.VISIBLE
                dataBinding.currentPlayerStarColor.visibility= View.INVISIBLE
                dataBinding.currentPlayerStar.visibility= View.INVISIBLE
            }
        }

        dataBinding.winnerText.visibility=View.INVISIBLE
    }

    private fun restart() {
        nextRound()
        totalPlayedRound=0
        starWinCount=0
        planetWinCount=0
        dataBinding.winMatchL.text=planetWinCount.toString()
        dataBinding.winMatchR.text=planetWinCount.toString()
        dataBinding.totalMatchL.text=totalPlayedRound.toString()
        dataBinding.totalMatchR.text=totalPlayedRound.toString()

        if (singlePlayer) dataBinding.playerSwitch.visibility= View.VISIBLE
    }

    private fun handlePlayerInput(view: ConstraintLayout) {
        if (entertainedComponents.size==0){
            dataBinding.playerSwitch.visibility= View.INVISIBLE
        }

        if(entertainedComponents.none { it.id == view.id } && !win) {
            if (player) {
                view.background =
                    AppCompatResources.getDrawable(this, R.drawable.mark_star)
            } else {
                view.background =
                    AppCompatResources.getDrawable(this, R.drawable.mark_planet)
            }
            entertainedComponents.add(PlayerTurnModel(player = player, id = view.id))
            checkWinState()
            if (win || draw) return
            switchPlayer()
            if (singlePlayer){
                if(myIcon!=player) computerMove()
            }
        } else if (singlePlayer && !win)
            if(myIcon!=player) computerMove()
    }

    private fun computerMove() {
        var view:ConstraintLayout?=null
        val allBoxList= listOf(R.id.top_left, R.id.top_center, R.id.top_right,
            R.id.left_center, R.id.center, R.id.right_center,
            R.id.bottom_left, R.id.bottom_center, R.id.bottom_right)
        val winningCombinations = listOf(
            // Rows
            listOf(R.id.top_left, R.id.top_center, R.id.top_right),
            listOf(R.id.left_center, R.id.center, R.id.right_center),
            listOf(R.id.bottom_left, R.id.bottom_center, R.id.bottom_right),
            // Columns
            listOf(R.id.top_left, R.id.left_center, R.id.bottom_left),
            listOf(R.id.top_center, R.id.center, R.id.bottom_center),
            listOf(R.id.top_right, R.id.right_center, R.id.bottom_right),
            // Diagonals
            listOf(R.id.top_left, R.id.center, R.id.bottom_right),
            listOf(R.id.top_right, R.id.center, R.id.bottom_left)
        )

//        find winning move
        for (combination in winningCombinations) {
            val cell1 = entertainedComponents.find { it.id == combination[0] }
            val cell2 = entertainedComponents.find { it.id == combination[1] }
            val cell3 = entertainedComponents.find { it.id == combination[2] }

            if (cell1 != null && cell2 != null && cell3 == null && cell1.player == cell2.player && cell1.player!=myIcon) {
                view =findViewById(combination[2])
            }
            else if (cell1 != null && cell3 != null && cell2 == null && cell1.player == cell3.player && cell1.player!=myIcon) {
                view =findViewById(combination[1])
            }
            else if (cell2 != null && cell3 != null && cell1 == null && cell2.player == cell3.player && cell2.player!=myIcon) {
                view =findViewById(combination[0])
            }
            if (view!=null){
                handlePlayerInput(view)
                return
            }
        }


//        find block move
        for (combination in winningCombinations) {
            val cell1 = entertainedComponents.find { it.id == combination[0] }
            val cell2 = entertainedComponents.find { it.id == combination[1] }
            val cell3 = entertainedComponents.find { it.id == combination[2] }

            if (cell1 != null && cell2 != null && cell3 == null && cell1.player == cell2.player) {
                view =findViewById(combination[2])
            }
            else if (cell1 != null && cell3 != null && cell2 == null && cell1.player == cell3.player) {
                view =findViewById(combination[1])
            }
            else if (cell2 != null && cell3 != null && cell1 == null && cell2.player == cell3.player) {
                view =findViewById(combination[0])
            }
            if (view!=null){
                handlePlayerInput(view)
                return
            }
        }

        //for random
        view=findViewById(allBoxList[(0 until 9).random()])
        handlePlayerInput(view)
    }


    private fun checkWinState() {
        val winState: Boolean?= checkWinner()
        var winner = ""
        if(winState==true) {
            winner = "Star"
            starWinCount++
            totalPlayedRound++
            dataBinding.winMatchR.text=starWinCount.toString()
            dataBinding.totalMatchR.text=totalPlayedRound.toString()
            dataBinding.totalMatchL.text=totalPlayedRound.toString()
        } else if(winState==false) {
            winner = "Planet"
            planetWinCount++
            totalPlayedRound++
            dataBinding.winMatchL.text=planetWinCount.toString()
            dataBinding.totalMatchL.text=totalPlayedRound.toString()
            dataBinding.totalMatchR.text=totalPlayedRound.toString()
        }
        if(winState!=null) {
            dataBinding.nextRoundBtn.visibility=View.VISIBLE
            Toast.makeText(this, "Player $winner Wins",Toast.LENGTH_SHORT).show()
            if (player!=myIcon && singlePlayer){
                dataBinding.winnerText.text="You Lose"
            }
            else if (player==myIcon && singlePlayer){
                dataBinding.winnerText.text="You Win"
            }
            else if (player && multiPlayer){
                dataBinding.winnerText.text="Star Wins "
            }
            else if (!player && multiPlayer){
                dataBinding.winnerText.text="Planet Wins "
            }
            dataBinding.winnerText.visibility=View.VISIBLE
        }
        else {
            checkDraw()
        }
    }


    private fun checkWinner(): Boolean? {
        val winningCombinations = listOf(
            // Rows
            listOf(R.id.top_left, R.id.top_center, R.id.top_right),
            listOf(R.id.left_center, R.id.center, R.id.right_center),
            listOf(R.id.bottom_left, R.id.bottom_center, R.id.bottom_right),
            // Columns
            listOf(R.id.top_left, R.id.left_center, R.id.bottom_left),
            listOf(R.id.top_center, R.id.center, R.id.bottom_center),
            listOf(R.id.top_right, R.id.right_center, R.id.bottom_right),
            // Diagonals
            listOf(R.id.top_left, R.id.center, R.id.bottom_right),
            listOf(R.id.top_right, R.id.center, R.id.bottom_left)
        )

        for (combination in winningCombinations) {
            val cell1 = entertainedComponents.find { it.id == combination[0] }
            val cell2 = entertainedComponents.find { it.id == combination[1] }
            val cell3 = entertainedComponents.find { it.id == combination[2] }

            if (cell1 != null && cell2 != null && cell3 != null &&
                cell1.player == cell2.player && cell1.player == cell3.player
            ) {
                win = true
                return cell1.player
            }
        }
        return null
    }


    private fun checkDraw() {
        if (entertainedComponents.size>=9){
            dataBinding.nextRoundBtn.visibility=View.VISIBLE
            totalPlayedRound++
            dataBinding.totalMatchL.text=totalPlayedRound.toString()
            dataBinding.totalMatchR.text=totalPlayedRound.toString()
            Toast.makeText(this, "Draw", Toast.LENGTH_SHORT).show()
            draw=true

            dataBinding.winnerText.text="Match Draw"
            dataBinding.winnerText.visibility=View.VISIBLE
        }
    }

    private fun switchPlayer() {
        player=!player
        if (player){
            dataBinding.currentPlayerPlanetColor.visibility=View.INVISIBLE
            dataBinding.currentPlayerPlanet.visibility=View.INVISIBLE
            dataBinding.currentPlayerStarColor.visibility=View.VISIBLE
            dataBinding.currentPlayerStar.visibility=View.VISIBLE
        }
        else{
            dataBinding.currentPlayerPlanetColor.visibility=View.VISIBLE
            dataBinding.currentPlayerPlanet.visibility=View.VISIBLE
            dataBinding.currentPlayerStarColor.visibility=View.INVISIBLE
            dataBinding.currentPlayerStar.visibility=View.INVISIBLE
        }
    }
}