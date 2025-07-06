import { useState, useEffect, useCallback } from 'react';
import AvatarCacheUtil from '../utils/avatarCache';

/**
 * 头像缓存自定义Hook
 * 封装头像缓存的获取和设置逻辑
 */
const useAvatarCache = () => {
  const [cacheStats, setCacheStats] = useState(null);

  /**
   * 获取用户头像，优先从缓存获取
   * @param {string|number} userId - 用户ID
   * @param {string} originalUrl - 原始头像URL
   * @returns {string} 头像URL
   */
  const getAvatarUrl = useCallback((userId, originalUrl) => {
    if (!userId || !originalUrl) {
      return originalUrl || '';
    }

    // 先尝试从缓存获取
    const cachedUrl = AvatarCacheUtil.getAvatar(userId);
    if (cachedUrl) {
      return cachedUrl;
    }

    // 如果缓存中没有，设置缓存并返回原始URL
    AvatarCacheUtil.setAvatar(userId, originalUrl);
    return originalUrl;
  }, []);

  /**
   * 批量处理活跃会员头像缓存
   * @param {Array} members - 活跃会员列表
   * @returns {Array} 处理后的会员列表
   */
  const processActiveMembersAvatars = useCallback((members) => {
    if (!Array.isArray(members)) {
      return [];
    }

    return members.map(member => {
      const cachedUrl = AvatarCacheUtil.getAvatar(member.id);
      if (cachedUrl) {
        return { ...member, avatar: cachedUrl };
      }

      // 如果没有缓存，设置缓存
      if (member.avatar) {
        AvatarCacheUtil.setAvatar(member.id, member.avatar);
      }

      return member;
    });
  }, []);

  /**
   * 预加载头像并缓存
   * @param {string|number} userId - 用户ID
   * @param {string} avatarUrl - 头像URL
   * @returns {Promise<boolean>} 是否预加载成功
   */
  const preloadAvatar = useCallback((userId, avatarUrl) => {
    return new Promise((resolve) => {
      if (!userId || !avatarUrl) {
        resolve(false);
        return;
      }

      // 检查是否已经缓存
      const cachedUrl = AvatarCacheUtil.getAvatar(userId);
      if (cachedUrl) {
        resolve(true);
        return;
      }

      // 预加载图片
      const img = new Image();
      img.onload = () => {
        // 图片加载成功后设置缓存
        const success = AvatarCacheUtil.setAvatar(userId, avatarUrl);
        resolve(success);
      };
      img.onerror = () => {
        resolve(false);
      };
      img.src = avatarUrl;
    });
  }, []);

  /**
   * 批量预加载头像
   * @param {Array} avatarList - 头像列表 [{userId, avatarUrl}, ...]
   * @returns {Promise<number>} 成功预加载的数量
   */
  const batchPreloadAvatars = useCallback(async (avatarList) => {
    if (!Array.isArray(avatarList)) {
      return 0;
    }

    const promises = avatarList.map(({ userId, avatarUrl }) => 
      preloadAvatar(userId, avatarUrl)
    );

    try {
      const results = await Promise.all(promises);
      return results.filter(success => success).length;
    } catch (error) {
      console.warn('Error in batch preload avatars:', error);
      return 0;
    }
  }, [preloadAvatar]);

  /**
   * 清除过期缓存
   * @returns {number} 清除的过期缓存数量
   */
  const clearExpiredCache = useCallback(() => {
    return AvatarCacheUtil.clearExpiredAvatars();
  }, []);

  /**
   * 清除所有缓存
   * @returns {number} 清除的缓存数量
   */
  const clearAllCache = useCallback(() => {
    return AvatarCacheUtil.clearAllAvatars();
  }, []);

  /**
   * 更新缓存统计信息
   */
  const updateCacheStats = useCallback(() => {
    const stats = AvatarCacheUtil.getCacheStats();
    setCacheStats(stats);
  }, []);

  /**
   * 手动设置头像缓存
   * @param {string|number} userId - 用户ID
   * @param {string} avatarUrl - 头像URL
   * @returns {boolean} 是否设置成功
   */
  const setAvatarCache = useCallback((userId, avatarUrl) => {
    return AvatarCacheUtil.setAvatar(userId, avatarUrl);
  }, []);

  /**
   * 移除特定用户的头像缓存
   * @param {string|number} userId - 用户ID
   * @returns {boolean} 是否移除成功
   */
  const removeAvatarCache = useCallback((userId) => {
    return AvatarCacheUtil.removeAvatar(userId);
  }, []);

  // 组件挂载时更新缓存统计
  useEffect(() => {
    updateCacheStats();
    
    // 清除过期缓存
    const expiredCount = clearExpiredCache();
    if (expiredCount > 0) {
      console.log(`Cleared ${expiredCount} expired avatar caches`);
      updateCacheStats();
    }
  }, [updateCacheStats, clearExpiredCache]);

  return {
    // 核心功能
    getAvatarUrl,
    processActiveMembersAvatars,
    
    // 预加载功能
    preloadAvatar,
    batchPreloadAvatars,
    
    // 缓存管理
    setAvatarCache,
    removeAvatarCache,
    clearExpiredCache,
    clearAllCache,
    updateCacheStats,
    
    // 统计信息
    cacheStats
  };
};

export default useAvatarCache;
