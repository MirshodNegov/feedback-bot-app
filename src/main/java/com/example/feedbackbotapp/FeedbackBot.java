package com.example.feedbackbotapp;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FeedbackBot extends TelegramLongPollingBot {
    private static final String USERNAME = "@xibei_feedback_bot";
    private static final String TOKEN = "6067611734:AAE3GvbsvTKFwwHZU9XYdPt0z7KbdiX_quo";
    private static final String CHATIDWITHASKAR = "1704645019";
    private static final String CHATIDWITHADMIN1 = "466048284";
    private static final String CHATIDWITHADMIN2 = "5253081731";
    private static final String CHATIDWITHME = "1145450707";
    private static HashMap<String, Integer> userLevels;
    private static HashMap<String, Integer> userLanguages;

    @Override
    public void onUpdateReceived(Update update) {
        Message message;
        String chatId = "";
        String text = "";
        String msgForAdmin = "";

        if (update.hasMessage()) {
            message = update.getMessage();
            chatId = message.getChatId().toString();
            if (message.hasText()) {
                text = message.getText();
                if (text.equalsIgnoreCase("/start")) {
                    if (userLevels == null)
                        userLevels = new HashMap<>();
                    userLevels.put(chatId, 0);
                }
            }
        }

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        SendMessage sendMessageAdmin1 = new SendMessage();
        sendMessageAdmin1.setChatId(CHATIDWITHADMIN1);
        SendMessage sendMessageAdmin2 = new SendMessage();
        sendMessageAdmin2.setChatId(CHATIDWITHADMIN2);
        SendMessage sendMessageMe = new SendMessage();
        sendMessageMe.setChatId(CHATIDWITHME);
        SendMessage sendMessageAskar = new SendMessage();
        sendMessageAskar.setChatId(CHATIDWITHASKAR);

        int level = 0;
        if (userLevels != null)
            if (userLevels.containsKey(chatId)) {
                level = userLevels.get(chatId);
            }
        switch (level) {
            case 0 -> {
                sendMessage.setText("""
                        \uD83C\uDDF7\uD83C\uDDFAЗдравствуйте! Добро пожаловать в наш бот!\s
                        Давайте для начала выберем язык обслуживания!
                               \s
                        \uD83C\uDDFA\uD83C\uDDFFAssalomu aleykum! Botimizga xush kelibsiz!\s
                        Keling, avvaliga xizmat ko’rsatish tilini tanlab olaylik.\s
                               \s
                        \uD83C\uDDEC\uD83C\uDDE7Hello! Welcome to our Bot!\s
                        Let's choose the language of service first
                        """);
                sendMessage.setReplyMarkup(selectLanguage());
                if (userLevels == null)
                    userLevels = new HashMap<>();
                userLevels.put(chatId, 1);
            }
            case 1 -> {
                if (text.equalsIgnoreCase("\uD83C\uDDF7\uD83C\uDDFA Русский")) {
                    sendMessage.setText("Спасибо, что решили поделиться мнением о наших блюдах и заведении. Для нас это важно!\n" +
                            "\n" +
                            "Отправьте свой отзыв в любом удобном вас виде. Заранее благодарим! \uD83E\uDD17");
                    if (userLanguages == null)
                        userLanguages = new HashMap<>();
                    userLanguages.put(chatId, 1);
                } else if (text.equalsIgnoreCase("\uD83C\uDDFA\uD83C\uDDFF O'zbek")) {
                    sendMessage.setText("Bizning taomlarimiz va muassasamiz haqida fikr bildirishni tanlaganingiz uchun tashakkur. Biz uchun bu muhim!\n" +
                            "\n" +
                            "Fikr-mulohazalaringizni o'zingiz uchun qulay bo'lgan istalgan shaklda yuboring. Oldindan rahmat! \uD83E\uDD17");
                    if (userLanguages == null)
                        userLanguages = new HashMap<>();
                    userLanguages.put(chatId, 2);
                } else if (text.equalsIgnoreCase("\uD83C\uDDEC\uD83C\uDDE7 English")) {
                    sendMessage.setText("Thank you for choosing to share your opinion about our dishes and establishment. For us it is important!\n" +
                            "\n" +
                            "Send your feedback in any form convenient for you. Thanks in advance! \uD83E\uDD17");
                    if (userLanguages == null)
                        userLanguages = new HashMap<>();
                    userLanguages.put(chatId, 3);
                }
                sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));
                if (userLevels == null)
                    userLevels = new HashMap<>();
                userLevels.put(chatId, 2);
            }
            case 2 -> {
                int lang = 1;
                if (userLanguages.containsKey(chatId)) {
                    lang = userLanguages.get(chatId);
                }
                switch (lang) {
                    case 1 ->
                            sendMessage.setText("Принято\uD83D\uDC4D Вы помогаете стать нам лучше! Спасибо \uD83D\uDC4D");
                    case 2 ->
                            sendMessage.setText("Qabul qilindi\uD83D\uDC4D Siz bizga yaxshilanishimizga yordam berding! Raxmat \uD83D\uDC4D");
                    case 3 ->
                            sendMessage.setText("Accepted\uD83D\uDC4D You help us become better! Thanks \uD83D\uDC4D");
                }

                User user = update.getMessage().getFrom();
                msgForAdmin = "\uD83D\uDC64 name : " + user.getFirstName() + "\n"
                        + "username : " + user.getUserName() + "\n"
                        + "feedback : " + text;
                sendMessageAdmin1.setText(msgForAdmin);
                sendMessageAdmin2.setText(msgForAdmin);
                sendMessageMe.setText(msgForAdmin);
                sendMessageAskar.setText(msgForAdmin);
            }
        }

        try {
            execute(sendMessage);
            if (level == 2) {
                execute(sendMessageAdmin1);
                execute(sendMessageAdmin2);
                execute(sendMessageMe);
                execute(sendMessageAskar);
            }
        } catch (
                TelegramApiException e) {
            e.printStackTrace();
        }

    }

    public static ReplyKeyboardMarkup selectLanguage() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setSelective(true);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(true);
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        KeyboardButton button1 = new KeyboardButton("\uD83C\uDDF7\uD83C\uDDFA Русский");
        KeyboardButton button2 = new KeyboardButton("\uD83C\uDDFA\uD83C\uDDFF O'zbek");
        row.add(button1);
        row.add(button2);
        KeyboardRow row2 = new KeyboardRow();
        KeyboardButton button3 = new KeyboardButton("\uD83C\uDDEC\uD83C\uDDE7 English");
        row2.add(button3);
        keyboard.add(row);
        keyboard.add(row2);
        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }

    @Override
    public String getBotUsername() {
        return USERNAME;
    }

    @Override
    public String getBotToken() {
        return TOKEN;
    }
}
