package ru.netology.nmedia.model

sealed interface FeedModelState {
    object Idle : FeedModelState
    object Loading : FeedModelState
    object Refreshing : FeedModelState
    object Error : FeedModelState
}
