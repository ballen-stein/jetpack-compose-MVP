package com.example.filmenthusiasts.mvp

interface MovieEnthusiastView<T> {
    fun setPresenter(presenter: T)
}