import { Button } from "@/components/ui/button";
import { Card } from "@/components/ui/card";
import {
    Form,
    FormControl,
    FormField,
    FormItem,
    FormLabel,
    FormMessage,
} from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import { zodResolver } from "@hookform/resolvers/zod";
import { createFileRoute, useNavigate } from "@tanstack/react-router";
import axios from "axios";
import { useAuth } from "../../providers/AuthProvider";
import { useState } from "react";
import { useForm } from "react-hook-form";
import { toast } from "react-toastify";
import { z } from "zod";
import {
    Select,
    SelectContent,
    SelectItem,
    SelectTrigger,
    SelectValue,
} from "@/components/ui/select";
import { useTranslation } from "react-i18next";

export const Route = createFileRoute("/_auth/createPost")({
    component: () => <CreatePost />,
});

const formSchema = z.object({
    title: z.string().min(2),
    imageUrl: z.instanceof(File).optional(),
    postType: z.boolean(),
    description: z.string(),
});

const CreatePost = () => {
    const [error, setError] = useState<string | null>(null);
    const [preview, setPreview] = useState<string | null>(null);
    const navigate = useNavigate();
    const auth = useAuth();
    const username = auth.user?.username || "";
    const { t } = useTranslation();

    const form = useForm<z.infer<typeof formSchema>>({
        resolver: zodResolver(formSchema),
        defaultValues: {
            title: "",
            imageUrl: null,
            postType: true,
            description: "",
        },
    });

    async function onSubmit(values: z.infer<typeof formSchema>) {
        const { title, imageUrl, postType, description } = values;
        // console.log(values);
        try {
            await axios
                .put(
                    "http://localhost:5000/api/posts",
                    { title, imageUrl, postType, description },
                    {
                        headers: {
                            "Content-Type": "multipart/form-data",
                            Authorization: username,
                        },
                    }
                )
                .catch((error) => {
                    console.log(error);
                })
                .then((response) => {
                    toast.success("Your post has been created");
                });
        } catch (error) {
            setError(error.message);
        }
    }
    return (
        <Card className="max-w-md mx-auto p-4">
            <h1 className="text-xl font-bold mb-4">{t("menu_create")}</h1>
            <Form {...form}>
                <form
                    onSubmit={form.handleSubmit(onSubmit)}
                    className="flex flex-col gap-4"
                >
                    <FormField
                        control={form.control}
                        name="title"
                        render={({ field }) => (
                            <FormItem>
                                <FormLabel>{t("title")}</FormLabel>
                                <FormControl>
                                    <Input {...field} />
                                </FormControl>
                                <FormMessage />
                            </FormItem>
                        )}
                    />

                    <FormField
                        control={form.control}
                        name="imageUrl"
                        render={({ field }) => (
                            <FormItem>
                                <FormLabel>{t("post_img")}</FormLabel>
                                <div className="flex flex-col gap-3">
                                    <FormControl>
                                        <Input
                                            className="cursor-pointer"
                                            type="file"
                                            accept="image/*" // This restricts file selection to image types
                                            onChange={(e) => {
                                                const file = e.target.files[0];
                                                if (
                                                    file &&
                                                    file.type.startsWith(
                                                        "image/"
                                                    )
                                                ) {
                                                    field.onChange(file);
                                                    setPreview(
                                                        URL.createObjectURL(
                                                            file
                                                        )
                                                    );
                                                    setError(null);
                                                } else {
                                                    field.onChange(null);
                                                    setPreview(null);
                                                    setError(
                                                        "Please select a valid image file."
                                                    );
                                                }
                                            }}
                                        />
                                    </FormControl>
                                    {preview && (
                                        <img
                                            src={preview}
                                            alt="Preview"
                                            className="w-full h-40 object-cover rounded-md"
                                        />
                                    )}
                                    <FormMessage />
                                </div>
                            </FormItem>
                        )}
                    />

                    <FormField
                        control={form.control}
                        name="postType"
                        render={({ field }) => (
                            <FormItem>
                                <FormLabel>{t("post_type")}</FormLabel>
                                <FormControl>
                                    <Select
                                        onValueChange={(value) =>
                                            field.onChange(value === "true")
                                        }
                                        defaultValue="true"
                                    >
                                        <SelectTrigger>
                                            <SelectValue placeholder="Post type" />
                                        </SelectTrigger>
                                        <SelectContent>
                                            <SelectItem value="false">
                                                {t("private")}
                                            </SelectItem>
                                            <SelectItem value="true">
                                                {t("public")}
                                            </SelectItem>
                                        </SelectContent>
                                    </Select>
                                </FormControl>
                                <FormMessage />
                            </FormItem>
                        )}
                    />
                    <FormField
                        control={form.control}
                        name="description"
                        render={({ field }) => (
                            <FormItem>
                                <FormLabel>{t("description")}</FormLabel>
                                <FormControl>
                                    <Textarea {...field} />
                                </FormControl>
                                <FormMessage />
                            </FormItem>
                        )}
                    />
                    {error && <FormMessage type="error">{error}</FormMessage>}

                    <Button variant="secondary" type="submit">
                        {t("menu_create")}
                    </Button>
                </form>
            </Form>
        </Card>
    );
};

export default CreatePost;
