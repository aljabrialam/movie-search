package appetisser.codingchallenge.itunes.ui.moviesearch

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import kotlinx.android.synthetic.main.activity_search.*
import appetisser.codingchallenge.itunes.R
import appetisser.codingchallenge.itunes.db.Movie
import appetisser.codingchallenge.itunes.ui.player.PlayerActivity
import co.lujun.androidtagview.TagContainerLayout
import co.lujun.androidtagview.TagView


class SearchActivity : AppCompatActivity() {

    companion object {
        const val TRACK_INDEX : String = "track_index"
        const val SEARCH_QUERY : String = "search_query"
        const val SEARCH_QUERY_DEFAULT_VALUE : String = "star"
    }

    private lateinit var searchViewModel : SearchViewModel
    private lateinit var adapter : ResultsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        supportActionBar?.elevation = 0f

        val tagContainerLayout = findViewById<TagContainerLayout>(R.id.tagcontainerLayout)
        tagContainerLayout.setOnTagClickListener(object : TagView.OnTagClickListener {

            override fun onTagClick(position: Int, text: String) {
                launchPlayerActivity(position, text)
            }

            override fun onTagLongClick(position: Int, text: String) {
                // do nothing
            }

            override fun onSelectedTagDrag(position: Int, text: String) {
                // do nothing
            }

            override fun onTagCrossClick(position: Int) {
                val movieTitle = tagContainerLayout.getTagText(position)
                confirmationDialog(movieTitle)
            }
        })

        // RecyclerView setup
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(DividerItemDecoration(recyclerView.context, LinearLayout.VERTICAL))

        adapter = ResultsAdapter(this) { selectedTrackIndex ->
            val i = Intent(this, PlayerActivity::class.java)
            i.putExtra(TRACK_INDEX, selectedTrackIndex)
            i.putExtra(SEARCH_QUERY, searchViewModel.searchQuery)
            startActivity(i)
        }
        recyclerView.adapter = adapter

        // Search input setup
        initSearchInputListener()

        // View model setup
        searchViewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)

        searchViewModel.results().observe(this, Observer { results ->
            progress.visibility = View.INVISIBLE
            adapter.submitItems(results)
            recyclerView.scrollToPosition(0)
            if(results.isEmpty()){
                text_error.text = getString(R.string.no_results)
                text_error.visibility = View.VISIBLE
            }else{
                text_error.visibility = View.INVISIBLE
            }
        })

        searchViewModel.error().observe(this, Observer { errorMessage ->
            progress.visibility = View.INVISIBLE
            text_error.visibility = View.VISIBLE
            text_error.text = errorMessage
        })

        searchViewModel.getAllMovies().observe(this, Observer<List<Movie>> {
            movies ->
            run {
                tagContainerLayout.tags = emptyList()
                tagContainerLayout.tags = movies.map { it.title }
            }
        })


    }

    private fun launchPlayerActivity(position: Int, title: String) {
        searchViewModel.setQuery(title)
        val i = Intent(this, PlayerActivity::class.java)
        i.putExtra(SEARCH_QUERY, title)
        startActivity(i)
    }

    private fun initSearchInputListener() {
        searchText.setOnEditorActionListener { view: View, actionId: Int, _: KeyEvent? ->
            when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    doSearch(view)
                    true
                }
                else -> false
            }
        }
        searchText.setOnKeyListener { view: View, keyCode: Int, event: KeyEvent ->
            when {
                event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER -> {
                    doSearch(view)
                    true
                }
                else -> false
            }
        }

    }

    private fun clear() {
        progress.visibility = View.VISIBLE
        text_error.visibility = View.INVISIBLE
        adapter.clearItems()
    }

    private fun doInitialSearch() {
        searchViewModel.setQuery(SEARCH_QUERY_DEFAULT_VALUE)
    }

    private fun doSearch(view: View) {
        progress.visibility = View.VISIBLE
        text_error.visibility = View.INVISIBLE
        adapter.clearItems()
        dismissKeyboard(view.windowToken)
        val query = searchText.text.toString()
        searchViewModel.setQuery(query)
    }

    private fun dismissKeyboard(windowToken: IBinder) {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(windowToken, 0)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        searchViewModel.sortResultsBy(item?.itemId!!)
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        if(searchText.text.isEmpty()) {
            doInitialSearch()
        } else {
            clear()
            searchViewModel.setQuery(searchText.text.toString())
        }
    }

    private fun confirmationDialog(title: String) {
        // Initialize a new instance of
        val builder = AlertDialog.Builder(this)
        // Set the alert dialog title
        builder.setTitle(getString(R.string.text_remove_tag))
        // Display a message on alert dialog
        builder.setMessage("Are you want to remove tag: $title?")
        // Set a positive button and its click listener on alert dialog
        builder.setPositiveButton(getString(R.string.text_yes)){ dialog, which ->
            searchViewModel.deleteByTitle(title)
        }
        // Display a negative button on alert dialog
        builder.setNegativeButton(getString(R.string.text_no)){ dialog, which ->
            dialog.dismiss()
        }
        // Finally, make the alert dialog using builder
        val dialog: AlertDialog = builder.create()
        // Display the alert dialog on app interface
        dialog.show()
    }

}
