import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { AuthProvider } from './contexts/AuthContext';
import Header from './components/Header';
import Home from './components/Home';
import TopicDetail from './components/TopicDetail';
import CreateTopic from './components/CreateTopic';
import EditTopic from './components/EditTopic';
import Login from './components/Login';
import OAuth2RedirectHandler from './components/OAuth2RedirectHandler';
import AvatarCacheTest from './components/AvatarCacheTest';

function App() {
  return (
    <AuthProvider>
      <Router>
        <div className="App">
          <Header />
          <div className="container">
            <Routes>
              <Route path="/" element={<Home />} />
              <Route path="/topic/:id" element={<TopicDetail />} />
              <Route path="/create-topic" element={<CreateTopic />} />
              <Route path="/edit-topic/:id" element={<EditTopic />} />
              <Route path="/login" element={<Login />} />
              <Route path="/oauth2/redirect" element={<OAuth2RedirectHandler />} />
              <Route path="/avatar-cache-test" element={<AvatarCacheTest />} />
            </Routes>
          </div>
        </div>
      </Router>
    </AuthProvider>
  );
}

export default App;
