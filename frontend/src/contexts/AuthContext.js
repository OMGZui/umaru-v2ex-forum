import { createContext, useContext, useState, useEffect } from 'react';
import api from '../services/api';

const AuthContext = createContext();

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [token, setToken] = useState(localStorage.getItem('token'));
  const [authChecked, setAuthChecked] = useState(false);

  useEffect(() => {
    // 防止重复检查认证状态
    if (authChecked) return;

    if (token) {
      api.defaults.headers.common['Authorization'] = `Bearer ${token}`;
      checkAuthStatus();
    } else {
      // 即使没有token，也要检查是否有OAuth session
      checkAuthStatus();
    }
  }, [token, authChecked]);

  const checkAuthStatus = async () => {
    try {
      const response = await api.get('/auth/user');
      // 由于我们的响应拦截器已经处理了 ApiResponse 格式，这里直接使用 response.data
      if (response.data && response.data.authenticated) {
        setUser(response.data.user);
        if (response.data.token) {
          setToken(response.data.token);
          localStorage.setItem('token', response.data.token);
        }
      }
    } catch (error) {
      console.error('Auth check failed:', error);
      logout();
    } finally {
      setLoading(false);
      setAuthChecked(true);
    }
  };

  const login = (userData, userToken) => {
    setUser(userData);
    setToken(userToken);
    localStorage.setItem('token', userToken);
    api.defaults.headers.common['Authorization'] = `Bearer ${userToken}`;
  };

  const logout = async () => {
    try {
      // 调用后端logout API来清理服务端状态
      if (token) {
        await api.post('/auth/logout');
      }
    } catch (error) {
      console.error('Logout API call failed:', error);
      // 即使API调用失败，也要继续清理本地状态
    } finally {
      // 清理本地状态
      setUser(null);
      setToken(null);
      setAuthChecked(false);
      localStorage.removeItem('token');
      delete api.defaults.headers.common['Authorization'];

      // 跳转到首页
      window.location.href = '/';
    }
  };

  const loginWithGitHub = () => {
    window.location.href = 'http://localhost:8080/oauth2/authorize/github';
  };

  const value = {
    user,
    loading,
    login,
    logout,
    loginWithGitHub,
    isAuthenticated: !!user
  };

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
};
