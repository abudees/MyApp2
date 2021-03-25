package com.example.myapp;

interface MyAsyncTask {
    Void doInBackground(String... urls);

    void onPostExecute(Void aVoid);
}
