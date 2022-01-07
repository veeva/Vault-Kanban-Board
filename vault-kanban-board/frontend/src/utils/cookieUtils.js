const buildCookieParams = () => {
  const now = new Date();
  now.setTime(now.getTime() + 30 * 60 * 60 * 24 * 1000);
  return {
    path: '/',
    expires: now,
    sameSite: 'none',
    secure: true,
  };
};

export default buildCookieParams;
