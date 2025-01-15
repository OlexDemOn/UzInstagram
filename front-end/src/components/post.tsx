import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import {
    ComponentPropsWithoutRef,
    ElementRef,
    forwardRef,
    useEffect,
    useState,
} from "react";
import {
    Collapsible,
    CollapsibleContent,
    CollapsibleTrigger,
} from "@/components/ui/collapsible";

import { FiMessageCircle } from "react-icons/fi";
import { FaHeart, FaRegHeart } from "react-icons/fa";
import { TPost } from "types/post";
import axios from "axios";
import { useAuth } from "../../providers/AuthProvider";
import { Textarea } from "./ui/textarea";
import { Button } from "./ui/button";
import { Link } from "@tanstack/react-router";
import { Badge } from "@/components/ui/badge";
import { FaRegBookmark, FaBookmark } from "react-icons/fa";
import { useTranslation } from "react-i18next";

export const PostContainer = forwardRef<
    ElementRef<"div">,
    ComponentPropsWithoutRef<"div"> & {
        post: TPost;
    }
>(({ post, children, className, ...props }, ref) => {
    const auth = useAuth();
    const username = auth.user?.username || "";
    const [isBookmarked, setIsBookmarked] = useState();
    const { t } = useTranslation();

    useEffect(() => {
        const bookmarks = JSON.parse(localStorage.getItem("bookmarks") || "[]");
        setIsBookmarked(bookmarks.includes(post.id));
    }, []);

    const handleBookmark = () => {
        let bookmarks = JSON.parse(localStorage.getItem("bookmarks") || "[]");
        if (isBookmarked) {
            bookmarks = bookmarks.filter((id) => id !== post.id);
        } else {
            bookmarks.push(post.id);
        }
        localStorage.setItem("bookmarks", JSON.stringify(bookmarks));
        setIsBookmarked((prev) => !prev);
    };

    return (
        <Card
            className={`relative min-h-60 w-full flex flex-col gap-5 p-5 ${className}`}
            ref={ref}
            {...props}
        >
            {children}
            <Collapsible>
                <div className="flex justify-between items-center gap-2">
                    <div className="flex items-center gap-2">
                        <Likes post={post} username={username} />
                        <CollapsibleTrigger>
                            <FiMessageCircle size={24} />
                        </CollapsibleTrigger>
                    </div>
                    <Badge variant={post.opened ? "secondary" : "default"}>
                        {post.opened ? t("public") : t("private")}
                    </Badge>
                </div>
                <Comments post={post} username={username} />
                {/* </div> */}
            </Collapsible>
            <div
                className="absolute top-5 right-5 cursor-pointer"
                onClick={handleBookmark}
            >
                {isBookmarked ? (
                    <FaBookmark size={24} />
                ) : (
                    <FaRegBookmark size={24} />
                )}
            </div>
        </Card>
    );
});

export const AvatarContainer = forwardRef<
    ElementRef<"div">,
    ComponentPropsWithoutRef<"div"> & {
        imgUrl: string;
        username?: string;
    }
>(({ imgUrl, username, className, ...props }, ref) => {
    return (
        <Link to={`/profile/${username}`}>
            <Avatar className={className} {...props} ref={ref}>
                <AvatarImage className="bg-cover" src={imgUrl} />
                <AvatarFallback>U</AvatarFallback>
            </Avatar>
        </Link>
    );
});

export const ImageContainer = forwardRef<
    ElementRef<"div">,
    ComponentPropsWithoutRef<"div"> & {
        post: TPost;
    }
>(({ post, className, ...props }, ref) => {
    return (
        <div
            className={`flex flex-col gap-2 ${className}`}
            {...props}
            ref={ref}
        >
            <p>{post.title}</p>
            {post.imageUrl !== "" && <img src={post.imageUrl} alt="Post" />}
            <>{post.description}</>
        </div>
    );
});

export const Likes = forwardRef<
    ElementRef<"div">,
    ComponentPropsWithoutRef<"div"> & {
        post: TPost;
        username: string;
    }
>(({ post, username, className, ...props }, ref) => {
    const [liked, setLiked] = useState(post.likedByCurrentUser);
    const [likesCount, setLikesCount] = useState<number>(post.likesCount);
    const likePost = () => {
        let link = "http://localhost:5000/api/posts/" + post.id;
        if (liked) {
            link += "/unlike";
        } else {
            link += "/like";
        }
        axios
            .post(
                link,
                {},
                {
                    headers: {
                        Authorization: `${username}`,
                    },
                }
            )
            .then((response) => {
                if (liked) {
                    setLikesCount((prev) => prev - 1);
                    setLiked(false);
                } else {
                    setLikesCount((prev) => prev + 1);
                    setLiked(true);
                }
            })
            .catch((error) => {
                console.error("Error liking post:", error);
            });
    };
    return (
        <div
            className={`flex items-center w-fit gap-2 hover:cursor-pointer size-12 ${className}`}
            onClick={likePost}
            ref={ref}
            {...props}
        >
            {likesCount ? <span>{likesCount}</span> : 0}
            {liked ? (
                <FaHeart size={24} color="red" />
            ) : (
                <FaRegHeart size={24} />
            )}
        </div>
    );
});

export const Comments = forwardRef<
    ElementRef<"div">,
    ComponentPropsWithoutRef<"div"> & {
        post: TPost;
        username: string;
    }
>(({ post, username, className, ...props }, ref) => {
    const [comments, setComments] = useState([]);
    const [newComment, setNewComment] = useState("");
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        axios
            .get(`http://localhost:5000/api/posts/${post.id}/comments`)
            .then((response) => {
                setComments(response.data.data);
            })
            .catch((error) => console.error(error));
    }, [post.id]);

    // Handle adding a new comment
    const handleAddComment = async () => {
        if (!newComment.trim()) return;
        setLoading(true);
        try {
            const response = await axios.post(
                `http://localhost:5000/api/posts/${post.id}/comments`,
                {
                    username: username,
                    content: newComment,
                }
            );
            if (comments == null || comments.length === 0) {
                setComments([response.data]);
            } else {
                setComments((prev) => [...prev, response.data]);
            }
            setNewComment("");
        } catch (error) {
            console.error("Error adding comment:", error);
        } finally {
            setLoading(false);
        }
    };

    return (
        <CollapsibleContent>
            <div className="space-y-5 mb-5">
                {comments && comments.length > 0 ? (
                    comments.map((comment) => (
                        <div key={comment.id} className="flex gap-2">
                            <AvatarContainer
                                imgUrl={comment.ownerProfileImageUrl}
                                username={comment.ownerUsername}
                            />
                            <div>
                                {comment.ownerUsername}
                                <p className="text-sm text-muted-foreground">
                                    {comment.content}
                                </p>
                            </div>
                        </div>
                    ))
                ) : (
                    <p className="text-muted-foreground">
                        No comments yet. Be the first to comment!
                    </p>
                )}
            </div>

            {/* Add Comment Section */}
            <div className="space-y-2">
                <Textarea
                    placeholder="Add a comment..."
                    value={newComment}
                    onChange={(e) => setNewComment(e.target.value)}
                    className="w-full"
                    disabled={loading}
                />
                <Button
                    onClick={handleAddComment}
                    disabled={loading || !newComment.trim()}
                    className="w-full"
                >
                    {loading ? "Posting..." : "Post Comment"}
                </Button>
            </div>
        </CollapsibleContent>
    );
});
