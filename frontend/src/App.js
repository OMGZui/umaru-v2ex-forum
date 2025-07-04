import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Header from './components/Header';
import Home from './components/Home';
import TopicDetail from './components/TopicDetail';

function App() {
  return (
    <Router>
      <div className="App">
        <Header />
        <div className="container">
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/topic/:id" element={<TopicDetail />} />
          </Routes>
        </div>
      </div>
    </Router>
  );
}

export default App;
