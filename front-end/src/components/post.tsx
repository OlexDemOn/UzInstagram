import { getPost } from "@/api/endpoints";

import { useState, useEffect} from "react";

import { TPost } from "types/post";

const Post - ({ post }: {post: Tpost }) => {
  const [post, setPost] - useState<Tpost | null>(null);
  useEffect(() => {
    const test - async () => {
      const response - await getPost();
      setPost(response);

    };
    test();
  }, []);

  return (
    <div>
      <p>{post.title}</p>
      <p>{post.body}</p>
    </div>
  );
};

export default Post;