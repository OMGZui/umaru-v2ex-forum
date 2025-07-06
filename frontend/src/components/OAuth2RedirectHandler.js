import { useEffect } from 'react';
import { useAuth } from '../contexts/AuthContext';
import { useNavigate } from 'react-router-dom';
import api from '../services/api';

const OAuth2RedirectHandler = () => {
  const { login } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    const urlParams = new URLSearchParams(window.location.search);
    const token = urlParams.get('token');
    const error = urlParams.get('error');

    console.log('OAuth2RedirectHandler - URL:', window.location.href);
    console.log('OAuth2RedirectHandler - token:', token);
    console.log('OAuth2RedirectHandler - error:', error);

    if (token) {
      // 使用token调用后端API获取完整用户信息
      try {
        // 先设置token到localStorage和axios header
        localStorage.setItem('token', token);

        // 设置axios默认header
        api.defaults.headers.common['Authorization'] = `Bearer ${token}`;

        // 调用后端API获取完整用户信息
        api.get('/auth/user')
        .then(response => {
          console.log('API response:', response);
          // 由于使用了我们的API服务，响应拦截器已经处理了ApiResponse格式
          const responseData = response.data;
          if (responseData.authenticated && responseData.user) {
            console.log('Login successful, navigating to home...');
            login(responseData.user, responseData.token || token);
            navigate('/', { replace: true });
          } else {
            console.error('Authentication failed:', responseData);
            navigate('/login?error=auth_failed', { replace: true });
          }
        })
        .catch(err => {
          console.error('Failed to get user info:', err);
          // 降级处理：解析JWT token获取基本用户信息
          const payload = JSON.parse(atob(token.split('.')[1]));
          const userData = {
            id: payload.sub,
            username: payload.username
          };
          login(userData, token);
          navigate('/', { replace: true });
        });
      } catch (err) {
        console.error('Token processing failed:', err);
        navigate('/login?error=invalid_token', { replace: true });
      }
    } else if (error) {
      console.error('OAuth2 error:', error);
      navigate('/login?error=' + encodeURIComponent(error), { replace: true });
    } else {
      navigate('/login?error=no_token', { replace: true });
    }
  }, [login, navigate]);

  return (
    <div style={{ 
      display: 'flex', 
      justifyContent: 'center', 
      alignItems: 'center', 
      height: '100vh',
      flexDirection: 'column'
    }}>
      <div>正在处理登录...</div>
      <div style={{ marginTop: '10px', fontSize: '14px', color: '#666' }}>
        请稍候，我们正在验证您的身份
      </div>
    </div>
  );
};

export default OAuth2RedirectHandler;
